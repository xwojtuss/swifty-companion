package edu.swifty.companion.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import edu.swifty.companion.BACKEND_V1_URL

@Composable
fun LoginScreen() {
    val uriHandler = LocalUriHandler.current

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Authenticate through Intra",
            )

            Button(
                onClick = {
                    // TODO: Make this redirect to the same tab
                    uriHandler.openUri("${BACKEND_V1_URL}/login")
                }
            ) {
                Text("Login")
            }
        }
    }
}