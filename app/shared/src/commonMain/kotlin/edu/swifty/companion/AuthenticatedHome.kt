package edu.swifty.companion

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText

class AuthenticatedHome {
    private val client = createHttpClient()

    suspend fun home(): String {
        val response = client.get(ApiConfig.getApiUrl(ApiConfig.Paths.HOME))
        val user: User = response.body()
        return user.name
    }
}