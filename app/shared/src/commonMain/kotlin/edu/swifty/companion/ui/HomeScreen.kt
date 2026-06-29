package edu.swifty.companion.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import edu.swifty.companion.AuthenticatedHome
import edu.swifty.companion.User

@Composable
fun HomeScreen(user: User? = null) {
    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            var authenticated by remember { mutableStateOf("Loading...") }
            LaunchedEffect(true) {
                authenticated = try {
                    AuthenticatedHome().home()
                } catch (e: Exception) {
                    e.message ?: "error"
                }
            }
            Text(authenticated)
        }
    }
}