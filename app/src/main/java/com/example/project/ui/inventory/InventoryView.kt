package com.example.project.ui.inventory

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project.data.IItemsRepo
import com.example.project.data.model.Item
import com.example.project.ui.SearchBar
import com.example.project.ui.ItemRow
import com.example.project.ui.theme.CustomTheme
import kotlinx.coroutines.async

@ExperimentalFoundationApi
@Composable
fun InventoryView(
    items: List<Item>,
    onAddItem: (Item) -> Unit,
    onUpdateItem: (Item, String) -> Unit,
    repository: IItemsRepo
) {
    val coroutineScope = rememberCoroutineScope()
    Box(
        contentAlignment = Alignment.Center,
    ) {

        Column {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top=10.dp, bottom = 10.dp).fillMaxWidth()
                ){
                Text(text = "Inventory", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = CustomTheme.colorScheme.onPrimary)
            }
            LazyColumn {
                items(items) { item ->
                    // boolean of weather the item is in the database
                    var inDatabaseState = remember(item.id) { mutableStateOf(false) }

                    // Launch a coroutine to check if the item is in the database using their IDs
                    LaunchedEffect(key1 = items) {
                        var isInDatabase = coroutineScope.async {
                            val itemsFromDb = repository.getItems()
                            itemsFromDb.any { it.id == item.id }
                        }.await()
                        // store the boolean of weather the item is in the database
                        inDatabaseState.value = isInDatabase
                    }
                    // display an ItemRow of the current item
                    ItemRow(item, onAddItem, inDatabaseState.value, onUpdateItem)
                }
            }
        }
    }
}


