package edu.swifty.companion

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform