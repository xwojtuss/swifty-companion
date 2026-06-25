package edu.swifty.companion

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText

class Greeting {
    private val client = HttpClient()

    suspend fun greet(): String {
        val response = client.get(ApiConfig.getApiUrl(ApiConfig.Paths.HELLO))
        return response.bodyAsText()
    }
}