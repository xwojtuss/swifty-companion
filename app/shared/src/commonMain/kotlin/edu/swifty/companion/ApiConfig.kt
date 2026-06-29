package edu.swifty.companion

object ApiConfig {
    const val BASE_URL = "http://localhost:8081/"
    const val API_PATH = "api/"
    const val API_VERSION = "v1"
    object Paths {
        const val HELLO = "hello"
        const val HOME = "me"
    }

    fun getApiUrl(path: String) = "$BASE_URL$API_PATH$API_VERSION/$path"
}