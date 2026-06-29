package edu.swifty.companion

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import edu.swifty.companion.ui.CallbackScreen
import edu.swifty.companion.ui.HomeScreen
import edu.swifty.companion.ui.LoginScreen
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

@Composable
@Preview
fun App() {
    val httpClient = HttpClient()
    val navController = rememberNavController()
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

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen()
        }
        composable("callback") {
            CallbackScreen()
        }
        composable("/") {
            HomeScreen()
        }
    }
}