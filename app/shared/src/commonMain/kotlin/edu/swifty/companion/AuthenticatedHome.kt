package edu.swifty.companion

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText

class AuthenticatedHome {
    private val client = HttpClient()

    suspend fun home(): String {
        val response = client.get(ApiConfig.getApiUrl(ApiConfig.Paths.HOME))
        return response.bodyAsText()
    }
}