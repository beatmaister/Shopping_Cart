package com.example.project.ui.cart

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.project.data.model.Item
import com.example.project.ui.CartItemRow
import com.example.project.ui.theme.CustomTheme

@ExperimentalFoundationApi
@Composable
fun CartListView(
    items: List<Item>,
    onDelete: (Item) -> Unit,
    onUpdateItem: (Item, String) -> Unit,
    totalAmount: Float,
    onDownload: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
    ) {
        Column {
            // Row displaying the total price and download button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween, // Adjusted arrangement
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total Price: $$totalAmount",
                    style = CustomTheme.typography.titleNormal,
                    color = CustomTheme.colorScheme.onPrimary,
                )
                Button(
                    onClick = { onDownload() }, // Call onDownload lambda when button is clicked
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CustomTheme.colorScheme.secondary,
                        contentColor = CustomTheme.colorScheme.onSecondary
                    )
                    ) {
                    Text("Save Shopping List")
                }
            }
            // LazyColumn to display the cart items
            LazyColumn {
                // Iterate through the items and display each item using CartItemRow
                items(items) { item ->
                    CartItemRow(item, onDelete, onUpdateItem)
                }
            }
        }
    }
}
