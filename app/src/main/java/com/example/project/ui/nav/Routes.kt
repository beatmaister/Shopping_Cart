package com.example.project.ui.nav

sealed class Routes(val route: String) {
    // Routes used by the navigation bar to display either the cart or inventory
    object CartList : Routes("cartlist")
    object Inventory : Routes("inventory")
}