package com.example.project.ui

import android.annotation.SuppressLint
import android.content.res.Resources
import android.content.res.Resources.Theme
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.project.ui.nav.Routes
import com.example.project.ui.nav.ItemsNav
import com.example.project.ui.theme.*

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@Composable
fun MainScreen() {
    // create a scaffold for the structure of the main screen
    val nav = rememberNavController()
    Scaffold(
        topBar = {
            TopBar()
        },
        bottomBar = {
            // adding the nav barr to the bottom of the screen
            BottomBar(nav = nav)
        }
    ) { pv: PaddingValues ->
        ItemsNav(nav, pv)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar() {
    // display the app title at the top of the screen
    TopAppBar(
        title = { Text("Shopping App") },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = CustomTheme.colorScheme.onPrimary,
            titleContentColor = CustomTheme.colorScheme.primary,
            navigationIconContentColor = CustomTheme.colorScheme.onPrimary,
            actionIconContentColor = CustomTheme.colorScheme.onSecondary
        )
    )
}

@Composable
private fun BottomBar(
    nav: NavHostController
) {
    // display the nav bar at the bottom, holding the routes in each option
    val currentBackStack by nav.currentBackStackEntryAsState()
    val currentRoute: String? = currentBackStack?.destination?.route
    NavigationBar(
        containerColor = CustomTheme.colorScheme.onSecondary,
        contentColor = CustomTheme.colorScheme.secondary
    ) {
        // nav option for the cart route
        NavigationBarItem(
            selected = currentRoute == Routes.CartList.route,
            onClick = {
                // navigate to cart when option is clicked
                nav.navigate(Routes.CartList.route) {
                    launchSingleTop = true
                    popUpTo(Routes.CartList.route)
                }
            },
            icon = {
                Icon(Icons.Default.ShoppingCart, "")
            },
            label = {
                Text("Cart")
            }
        )
        // nav option for the inventory route
        NavigationBarItem(
            selected = currentRoute == Routes.Inventory.route,
            onClick = {
                // navigate to inventory when option is clicked
                nav.navigate(Routes.Inventory.route) {
                    launchSingleTop = true
                }
            },
            icon = {
                Icon(Icons.Default.Add, "")
            },
            label = {
                Text("Inventory")
            }
        )
    }
}