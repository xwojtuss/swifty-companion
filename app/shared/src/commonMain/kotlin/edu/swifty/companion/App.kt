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
            val user = httpClient.get("${BACKEND_V1_URL}/me").body<User>()
            AuthState.Authenticated(user)
        } catch (e: Exception) {
            AuthState.Unauthenticated
        }
    }

    when (val state = authState) {
        is AuthState.Loading -> CircularProgressIndicator()
        is AuthState.Unauthenticated -> LoginScreen()
        is AuthState.Authenticated -> HomeScreen(state.user)
    }
}