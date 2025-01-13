package com.example.project.ui.inventory

import android.Manifest
import android.app.Activity
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.project.MainActivity
import com.example.project.data.IItemsRepo
import com.example.project.data.model.Item
import com.example.project.network.IItemsFetcher
import kotlinx.coroutines.launch


class InventoryViewModel(
    app: Application,
    private val activity: Activity,
    private val itemsFetcher: IItemsFetcher,
    private val repository: IItemsRepo
) : AndroidViewModel(app) {
    private val _items: MutableState<List<Item>> = mutableStateOf(listOf())
    val items: State<List<Item>> = _items

    init {
        // initialize the inventory list
        viewModelScope.launch {
            fetchItemsFromApi()
        }
    }

    private fun fetchItemsFromApi() {
        // fetch inventory items from the API
        viewModelScope.launch {
            _items.value = itemsFetcher.fetchItems() // Fetch items from the API
            Log.d("InventoryViewModel", "Items fetched: ${items.value}")
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun addItem(item: Item) {
        Log.e(TAG, "Adding item")
        viewModelScope.launch {
            val wasEmpty = repository.getItems().isEmpty() // check if database is currently empty
            repository.addItem(item) // Add the item to the database
            fetchItemsFromApi() // Refresh the list after update

            val intent = Intent(activity, MainActivity::class.java)
            if (wasEmpty && repository.getItems().isNotEmpty()) { // if the database was first empty, send a notification
                // Check if permission is granted, and send one if it is
                if (ActivityCompat.checkSelfPermission(
                        activity,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    createNotification()
                } else {
                    // if it is not granted, send the request for permission
                    ActivityCompat.requestPermissions(
                        activity,
                        arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                        PERMISSION_REQUEST_CODE
                    )
                }
            }
        }
    }

    fun updateItem(item: Item, amount: String) {
        viewModelScope.launch {
            item.amount = amount
            repository.updateItem(item, amount) // Update item amount in the database
            fetchItemsFromApi() // Refresh the list after update
        }
    }

    private fun createNotification() {
        createNotificationChannel() // create the channel for the cart notification
        val intent = Intent(activity, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(activity, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)
        // build the text notification
        val builder = NotificationCompat.Builder(activity, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_menu_info_details)
            .setContentTitle("Cart Reminder")
            .setContentText(NOTIFICATION_TEXT)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notification = builder.build() // store the built notification in variable
        // check if permission is granted and send one if it is
        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat.from(activity).notify(NOTIFICATION_ID, notification)
        }
    }

    private fun createNotificationChannel() {
        // create notification channel for version codes above 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Cart Notification"
            val descriptionText = "Notification channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                activity.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    // object to store all notification related constants
    companion object {
        private const val TAG = "InventoryViewModel"
        private const val CHANNEL_ID = "com.example.project.ui.inventory"
        private const val NOTIFICATION_ID = 1
        private const val NOTIFICATION_TEXT = "You have items in your cart"
        private const val PERMISSION_REQUEST_CODE = 101
    }

}

