package edu.swifty.companion

import io.ktor.client.call.body
import io.ktor.client.request.get

class UserProfile {
    private val client = createHttpClient()

    suspend fun me(): User {
        val response = client.get(ApiConfig.getApiUrl(ApiConfig.Paths.ME))
        val user: User = response.body()
        return user
    }
}