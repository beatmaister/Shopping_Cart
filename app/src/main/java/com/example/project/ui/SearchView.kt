package com.example.project.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(onFilter: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        var searchText: String by remember { mutableStateOf("") }
        OutlinedTextField(
            modifier = Modifier.weight(2.5f),
            value = searchText,
            onValueChange = { v: String ->
                searchText = v
            },
            placeholder = {
                Text("Search")
            },
            singleLine = true
        )
        Button(onClick = {
            onFilter(searchText)
        }, modifier = Modifier.padding(start=10.dp).weight(1f)) {
            Icon(Icons.Default.Search, "Search")
        }
    }
}
