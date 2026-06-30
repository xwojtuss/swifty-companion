package edu.swifty.companion

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute

import edu.swifty.companion.ui.LoginScreen
import edu.swifty.companion.ui.SearchScreen
import edu.swifty.companion.ui.UserScreen
import io.ktor.client.call.body
import io.ktor.client.request.get

@Composable
@Preview
fun App() {
    val httpClient = remember { createHttpClient() }
    var authState by remember { mutableStateOf<AuthState>(AuthState.Loading) }
    val navController = rememberNavController()

    LaunchedEffect(Unit) {
        authState = try {
            val user = httpClient.get("${BACKEND_V1_URL}/${ApiConfig.Paths.ME}").body<User>()
            AuthState.Authenticated(user)
        } catch (e: Exception) {
            AuthState.Unauthenticated
        }
    }



    NavHost(navController = navController, startDestination = Home) {
        composable<UserRoute> { backStackEntry ->
            when (val state = authState) {
                is AuthState.Loading -> CircularProgressIndicator()
                is AuthState.Unauthenticated -> LoginScreen()
                is AuthState.Authenticated -> UserScreen(backStackEntry.toRoute<UserRoute>().displayName, httpClient)
            }
        }

        composable<Home> {
            when (val state = authState) {
                is AuthState.Loading -> CircularProgressIndicator()
                is AuthState.Unauthenticated -> LoginScreen()
                is AuthState.Authenticated -> SearchScreen(navController)
            }
        }
    }
}