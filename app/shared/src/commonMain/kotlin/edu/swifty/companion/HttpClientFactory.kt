package edu.swifty.companion

import io.ktor.client.HttpClient

expect fun createHttpClient(): HttpClient
