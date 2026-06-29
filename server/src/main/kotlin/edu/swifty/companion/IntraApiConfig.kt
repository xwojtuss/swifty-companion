package edu.swifty.companion

object IntraApiConfig {
    const val BASE_URL = "https://api.intra.42.fr/"
    const val API_VERSION = "v2"

    object Paths {
        const val ME = "me"
    }

    fun getApiUrl(path: String) = "$BASE_URL$API_VERSION/$path"
}