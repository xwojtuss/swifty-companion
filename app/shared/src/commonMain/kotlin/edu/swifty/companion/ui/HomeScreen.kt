package edu.swifty.companion.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import edu.swifty.companion.User

@Composable
fun HomeScreen(user: User) {
    MaterialTheme {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column (
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = user.displayName)
                Text(text = user.email)
                Text(text = "Is Staff: ${user.isStaff}")
                Text(text = "Is Active: ${user.isActive}")
                Text(text = "Is Alumni: ${user.isAlumni}")
            }
        }
    }
}
