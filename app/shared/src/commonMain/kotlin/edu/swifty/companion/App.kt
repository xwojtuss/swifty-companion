package edu.swifty.companion

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.resources.painterResource

import companion.app.shared.generated.resources.Res
import companion.app.shared.generated.resources.compose_multiplatform
import edu.swifty.companion.ui.CallbackScreen
import edu.swifty.companion.ui.HomeScreen
import edu.swifty.companion.ui.LoginScreen

@Composable
@Preview
fun App() {
    val navController = rememberNavController()

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