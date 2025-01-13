package com.example.project.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project.data.model.Item
import com.example.project.ui.theme.CustomTheme

@ExperimentalFoundationApi
@Composable
fun CartItemRow(
    item: Item,
    onDelete: (Item) -> Unit,
    onUpdateItem: (Item, String) -> Unit
) {
    // local variable to hold the current amount of the item in the database
    var amount by remember { mutableStateOf(item.amount) }

    // Trigger recomposition when the amount changes
    LaunchedEffect(item.amount) {
        amount = item.amount
    }

    // card item holding the rows of each cart item
    Card(
        shape = RoundedCornerShape(5.dp),
        colors = CardDefaults.cardColors(
            containerColor = CustomTheme.colorScheme.primary,
            contentColor = CustomTheme.colorScheme.onPrimary
        ),
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, top = 5.dp, bottom = 5.dp)
            .fillMaxWidth()
    ) {
        // Each row in the cart and their respective data
        Row(
            modifier = Modifier.padding(6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(
                modifier = Modifier.weight(1.8f)
            ) {
                // row displaying all the item's stored information
                Row(
                    modifier = Modifier.padding(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Name:", modifier = Modifier.weight(.70f))
                    Text(item.name, modifier = Modifier.weight(2.0f), fontSize = 28.sp, color = MaterialTheme.colorScheme.secondary)
                }
                Row(
                    modifier = Modifier.padding(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Price:", modifier = Modifier.weight(.70f))
                    Text("$" + item.price, modifier = Modifier.weight(2.0f))
                }
                Row(
                    modifier = Modifier.padding(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Desc.:", modifier = Modifier.weight(.70f))
                    Text(item.description, modifier = Modifier.weight(2.0f))
                }
            }
            Column(
                modifier = Modifier.weight(1.0f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // delete button to remove items from the database
                Button(
                    onClick = { onDelete(item) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CustomTheme.colorScheme.secondary,
                        contentColor = CustomTheme.colorScheme.onSecondary
                    ),
                    modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Default.Delete, "Delete Item")
                }
                Spacer(modifier = Modifier.padding(bottom=5.dp))

                // Display the current amount of the item
                Row() {
                    Text("Amount: ")
                    Text(amount)
                }
                Row() {
                    // button to increase amount of item
                    IconButton(
                        onClick = {
                            val newAmount = amount.toInt() + 1
                            amount = newAmount.toString()
                            onUpdateItem(item, amount) // Update item amount in the ViewModel
                        },
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Icon(Icons.Default.KeyboardArrowUp, "Increase Amount")
                    }
                    // button to decrease amount of item. capped at 1
                    IconButton(
                        onClick = {
                            val newAmount = amount.toInt() - 1
                            if (newAmount > 0) {
                                amount = newAmount.toString()
                                onUpdateItem(item, amount) // Update item amount in the ViewModel
                            }else{
                                onUpdateItem(item, "1")
                            }
                        },
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Icon(Icons.Default.KeyboardArrowDown, "Decrease Amount")
                    }
                }
            }
        }
    }
}



