package edu.swifty.companion.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import io.ktor.websocket.Frame

@Composable
fun DisplayNameSearch(
    onSearch: (String) -> Unit
) {
    var query by remember { mutableStateOf("") }

    TextField(
        value = query,
        onValueChange = { query = it },
        placeholder = { Frame.Text("Search") },
        singleLine = true,
        trailingIcon = {
            IconButton(onClick = { onSearch(query) }) {
                Icon(Icons.Default.Search, contentDescription = "Search")
            }
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearch(query)
            }
        )
    )
}