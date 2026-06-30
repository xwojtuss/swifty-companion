package edu.swifty.companion

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview

import edu.swifty.companion.ui.HomeScreen
import edu.swifty.companion.ui.LoginScreen
import io.ktor.client.call.body
import io.ktor.client.request.get

@Composable
@Preview
fun App() {
    val httpClient = remember { createHttpClient() }
    var authState by remember { mutableStateOf<AuthState>(AuthState.Loading) }

    LaunchedEffect(Unit) {
        authState = try {
            val user = httpClient.get("${BACKEND_V1_URL}/${ApiConfig.Paths.ME}").body<User>()
            AuthState.Authenticated(user)
        } catch (e: Exception) {
            AuthState.Unauthenticated
        }
    }

    var searchedUser by remember { mutableStateOf<User?>(null) }

    LaunchedEffect(Unit) {
        searchedUser = try {
            httpClient.get("${BACKEND_V1_URL}/${ApiConfig.Paths.USERS}/dbozic").body<User>()
        } catch (e: Exception) {
            null
        }
    }

    when (val state = authState) {
        is AuthState.Loading -> CircularProgressIndicator()
        is AuthState.Unauthenticated -> LoginScreen()
        is AuthState.Authenticated -> HomeScreen(searchedUser)
    }
}