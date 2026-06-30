package edu.swifty.companion.ui

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import edu.swifty.companion.ApiConfig
import edu.swifty.companion.BACKEND_V1_URL
import edu.swifty.companion.User
import edu.swifty.companion.UserRoute
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(navigator: NavController) {
    DisplayNameSearch { query ->
        navigator.navigate(UserRoute(query))
    }
}