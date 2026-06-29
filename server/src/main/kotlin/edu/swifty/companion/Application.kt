package edu.swifty.companion

import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.OAuth2RedirectError
import io.ktor.server.auth.OAuthServerSettings
import io.ktor.server.auth.oauth
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.Sessions
import io.ktor.server.sessions.cookie
import io.ktor.util.collections.ConcurrentMap
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation as ClientContentNegotiation
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.auth.OAuthAccessTokenResponse
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.sessions.get
import io.ktor.server.sessions.sessions
import io.ktor.server.sessions.set
import kotlinx.serialization.json.Json

val applicationHttpClient: HttpClient = HttpClient(CIO) {
    install(ClientContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
        })
    }
}

fun main() {
    embeddedServer(Netty, port = BACKEND_PORT, host = DOMAIN, module = Application::module)
        .start(wait = true)
}

fun Application.module(httpClient: HttpClient = applicationHttpClient) {
    install(ContentNegotiation) {
        json()
    }

    install(CORS) {
        allowHost("${DOMAIN}:${FRONTEND_PORT}")
        allowHost("${DOMAIN}:${BACKEND_PORT}")

        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Options)

        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Authorization)
        allowHeader(HttpHeaders.AccessControlAllowOrigin)
        allowCredentials = true
    }

    install(Sessions) {
        cookie<UserSession>("user_session") {
            cookie.httpOnly = true
            cookie.secure = true
            cookie.maxAgeInSeconds = 3600
            cookie.path = "/"
            cookie.extensions["SameSite"] = "None"
        }
    }

    val redirects = ConcurrentMap<String, String>()

    install(Authentication) {
        oauth("auth-oauth-42intra") {
            // Configure oauth authentication
            urlProvider = { "${BACKEND_V1_URL}/callback" }
            settings = OAuthServerSettings.OAuth2ServerSettings(
                name = "42intra",
                authorizeUrl = "${INTRA_OAUTH_URL}/authorize",
                accessTokenUrl = "${INTRA_OAUTH_URL}/token",
                requestMethod = HttpMethod.Post,
                clientId = INTRA_CLIENT_ID,
                clientSecret = System.getenv("42INTRA_CLIENT_SECRET").orEmpty(),
                defaultScopes = listOf("public"),
                onStateCreated = { call, state ->
                    //saves new state with redirect url value
                    call.request.queryParameters["redirectUrl"]?.let {
                        redirects[state] = it
                    }
                },
            )
            fallback = { cause ->
                if (cause is OAuth2RedirectError) {
                    respondRedirect("/login-after-fallback")
                } else {
                    respond(HttpStatusCode.Forbidden, cause.message)
                }
            }
            client = httpClient
        }
    }

    routing {
        route("/api/v1") {
            get("/hello") {
                call.respondText(sayHello("Ktor"))
            }

            authenticate("auth-oauth-42intra") {
                get("/login") {
                    // Redirects to 'authorizeUrl' automatically
                }

                get("/callback") {
                    val currentPrincipal: OAuthAccessTokenResponse.OAuth2? = call.principal()
                    // redirects home if the url is not found before authorization
                    currentPrincipal?.let { principal ->
                        principal.state?.let { state ->
                            call.sessions.set(UserSession(state, principal.accessToken))
                            redirects.remove(state)?.let { redirect ->
                                call.respondRedirect(redirect)
                                return@get
                            }
                        }
                    }
                    call.respondRedirect(FRONTEND_BASE_URL)
                }
            }

            get("/me") {
                val session = call.sessions.get<UserSession>()
                    ?: return@get call.respond(HttpStatusCode.Unauthorized)
                val response = httpClient.get(IntraApiConfig.getApiUrl(IntraApiConfig.Paths.ME)) {
                    header(HttpHeaders.Authorization, "Bearer ${session.token}")
                }
                val user: User = response.body()
                call.respond(user)
            }

        }
    }
}