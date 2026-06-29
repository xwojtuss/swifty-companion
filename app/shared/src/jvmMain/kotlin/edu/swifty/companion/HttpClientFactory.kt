package edu.swifty.companion

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

actual fun createHttpClient(): HttpClient = HttpClient {
    install(HttpCookies)
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true })
    }
}
