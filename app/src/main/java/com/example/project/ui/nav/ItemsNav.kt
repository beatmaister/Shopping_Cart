package com.example.project.ui.nav

import android.app.Activity
import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.project.data.impl.ItemsDatabaseRepository
import com.example.project.network.ItemsFetcher
import com.example.project.ui.inventory.InventoryView
import com.example.project.ui.inventory.InventoryViewModel
import com.example.project.ui.cart.CartListView
import com.example.project.ui.cart.CartListViewModel

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@ExperimentalFoundationApi
@Composable
fun ItemsNav(
    navController: NavHostController = rememberNavController(),
    paddingValues: PaddingValues
) {
    val activity = LocalContext.current as Activity // Get the activity context

    // Navigation host for handling different screens
    NavHost(
        navController = navController,
        startDestination = Routes.CartList.route,
        modifier = Modifier.padding(paddingValues)
    ) {
        // Composable for displaying the cart list screen
        composable(Routes.CartList.route) {
            val application = LocalContext.current.applicationContext as Application
            val repository = ItemsDatabaseRepository(application)
            val vm: CartListViewModel = remember(application) {
                CartListViewModel(application, repository)
            }
            val items by vm.items
            val totalAmount by vm.totalAmount

            // Display the cart list view
            CartListView(
                items,
                onDelete =vm::deleteItem,
                onUpdateItem = vm::updateItem,
                totalAmount = totalAmount,
                onDownload = {
                    // Build the shopping list text
                    val shoppingList = buildString {
                        for (item in items) {
                            append("${item.name}: ${item.price}, ${item.amount}\n")
                        }
                        append("Total Balance: $$totalAmount")
                    }
                    // Create document to save in device
                    vm.createNoteAsDocument(activity, shoppingList)
                }
            )
        }

        // Composable for displaying the inventory screen
        composable(Routes.Inventory.route) {
            val application = LocalContext.current.applicationContext as Application
            val itemsFetcher = ItemsFetcher()
            val repository = ItemsDatabaseRepository(application)
            val vm: InventoryViewModel = remember(application) {
                InventoryViewModel(application, activity, itemsFetcher, repository)
            }
            val items by vm.items

            // Display the inventory view
            InventoryView(
                items = items,
                onAddItem = vm::addItem,
                onUpdateItem = vm::updateItem,
                repository = repository
                )
        }
    }
}