package edu.swifty.companion

import io.ktor.client.HttpClient
import io.ktor.client.engine.js.Js
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

@JsFun("(init) => { init.credentials = 'include'; }")
private external fun setCredentials(init: JsAny): Unit

actual fun createHttpClient(): HttpClient = HttpClient(Js) {
    engine {
        configureRequest {
            setCredentials(this)
        }
    }
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true })
    }
}
