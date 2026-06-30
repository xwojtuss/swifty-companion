package edu.swifty.companion.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import edu.swifty.companion.ApiConfig
import edu.swifty.companion.BACKEND_V1_URL
import edu.swifty.companion.User
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.launch

@Composable
fun UserScreen(displayName: String, httpClient: HttpClient) {
    val scope = rememberCoroutineScope()
    var searchedUser by remember { mutableStateOf<User?>(null) }
    var loading by remember { mutableStateOf(false) }
    var notFound by remember { mutableStateOf(false) }

    scope.launch {
        loading = true
        notFound = false

        searchedUser = try {
            httpClient
                .get("$BACKEND_V1_URL/${ApiConfig.Paths.USERS}/$displayName")
                .body<User>()
        } catch (e: Exception) {
            notFound = true
            null
        }

        loading = false
    }

    when {
        loading -> {
            CircularProgressIndicator()
        }

        searchedUser != null -> {
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
                        Text(text = searchedUser!!.displayName)
                        Text(text = searchedUser!!.email)
                        Text(text = "Is Staff: ${searchedUser!!.isStaff}")
                        Text(text = "Is Active: ${searchedUser!!.isActive}")
                        Text(text = "Is Alumni: ${searchedUser!!.isAlumni}")
                    }
                }
            }
        }

        notFound -> {
            Text("Not found")
        }
    }
}
