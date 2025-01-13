package com.example.project.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
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
fun ItemRow(
    item: Item,
    onAddItem: (Item) -> Unit,
    isInDatabase: Boolean,
    onUpdateItem: (Item, String) -> Unit,
) {
    var inDatabase = isInDatabase // hold the boolean for if the item is currently in the database
    //mutable states holding booleans for whether to display each dialog box
    val (showDatabaseDialog, setShowDatabaseDialog) = remember { mutableStateOf(false) }
    val (showAvailabilityDialog, setShowAvailabilityDialog) = remember { mutableStateOf(false) }

    // Main card of each inventory Item
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
        // Main row holding all inventory items data
        Row(
            modifier = Modifier.padding(6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(
                modifier = Modifier.weight(1.8f)
            ) {
                // row holding all database stored info of each item
                Row(
                    modifier = Modifier.padding(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Name:", modifier = Modifier.weight(.70f))
                    Text(item.name, modifier = Modifier.weight(2.0f), fontSize = 28.sp)
                }
                Row(
                    modifier = Modifier.padding(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Price:", modifier = Modifier.weight(.70f))
                    Text(item.price, modifier = Modifier.weight(2.0f))
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
                // button to add each item to the database (cart)
                // checking if it is already in and displaying a dialog with the choice to cancel or increment
                Button(onClick= {
                    if (inDatabase) {
                        setShowDatabaseDialog(true)
                    }else if (!item.isAvailable) {
                        setShowAvailabilityDialog(true)
                    } else {
                        onAddItem(item)
                        inDatabase = true;
                    }
                                },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CustomTheme.colorScheme.secondary,
                        contentColor = CustomTheme.colorScheme.onSecondary
                    ),
                    modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Default.ShoppingCart, "Add to Cart")
                }
                Spacer(modifier = Modifier.padding(bottom=5.dp))
                Row(            verticalAlignment = Alignment.CenterVertically,
                ) {
                    Checkbox(
                        checked = item.isAvailable,
                        colors = CheckboxDefaults.colors(
                            checkedColor = CustomTheme.colorScheme.secondary,
                            uncheckedColor = CustomTheme.colorScheme.onSecondary
                        ),
                        onCheckedChange = {},
                        modifier = Modifier.padding(end=5.dp)
                    )
                    Text("In Stock")
                }
                // display the dialog if an item is already in the cart
                if (showDatabaseDialog) {
                    AlertDialog(
                        onDismissRequest = { setShowDatabaseDialog(false) },
                        confirmButton = {
                            // increments the item's amount by 1 in the cart if clicked
                            Button(onClick = {
                                var amount = item.amount.toFloat()
                                amount++
                                onUpdateItem(item, amount.toString())
                                setShowDatabaseDialog(false)
                            }) {
                                Text("Add Another")
                            }
                        },
                        dismissButton = {
                            // dismisses the dialog if cancelled
                            Button(onClick = { setShowDatabaseDialog(false) }) {
                                Text("Cancel")
                            }
                        },
                        title = {
                            Text("Item Already in Cart")
                        },
                        text = {
                            Text("This item is already in your cart. Do you want to increment the quantity by 1?")
                        }
                    )
                }

                if (showAvailabilityDialog) {
                    // Dialog for item not available /  out-of-stock
                    AlertDialog(
                        onDismissRequest = { setShowAvailabilityDialog(false) },
                        confirmButton = {
                            // simply dismisses the dialog box when clicked
                            Button(onClick = { setShowAvailabilityDialog(false) }) {
                                Text("OK")
                            }
                        },
                        title = {
                            Text("Item Not Available")
                        },
                        text = {
                            Text("This item is currently out of stock.")
                        }
                    )
                }
            }
        }
    }
}