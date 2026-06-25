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
import io.ktor.server.http.content.staticResources
import io.ktor.server.netty.*
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.request.uri
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.Sessions
import io.ktor.server.sessions.cookie
import io.ktor.util.collections.ConcurrentMap
import io.ktor.client.*
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType.Application.Json
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.auth.OAuthAccessTokenResponse
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.sessions.sessions
import io.ktor.server.sessions.set
import kotlinx.serialization.json.Json

val applicationHttpClient = HttpClient(CIO) {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
        })
    }
}

fun main() {
    embeddedServer(Netty, port = 8081, host = "localhost", module = Application::module)
        .start(wait = true)
}

fun Application.module(httpClient: HttpClient = applicationHttpClient) {
    install(CORS) {
        allowHost("localhost:8080")

        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Options)

        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Authorization)

        allowCredentials = true
    }

    install(Sessions) {
        cookie<UserSession>("user_session")
    }

    val redirects = ConcurrentMap<String, String>()

    install(Authentication) {
        oauth("auth-oauth-42intra") {
            // Configure oauth authentication
            urlProvider = { "http://localhost:8080/callback" }
            settings = OAuthServerSettings.OAuth2ServerSettings(
                name = "42intra",
                authorizeUrl = "https://api.intra.42.fr/oauth/authorize",
                accessTokenUrl = "https://api.intra.42.fr/oauth/token",
                requestMethod = HttpMethod.Post,
                clientId = System.getenv("42INTRA_CLIENT_ID").orEmpty(),
                clientSecret = System.getenv("42INTRA_CLIENT_SECRET").orEmpty(),
//                defaultScopes = listOf("https://www.googleapis.com/auth/userinfo.profile"),
                extraAuthParameters = listOf("access_type" to "offline"),
                onStateCreated = { call, state ->
                    //saves new state with redirect url value
                    call.request.queryParameters["redirectUrl"]?.let {
                        redirects[state] = it
                    }
                }
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
                    call.respondRedirect("/home")
                }
            }
        }
    }
}