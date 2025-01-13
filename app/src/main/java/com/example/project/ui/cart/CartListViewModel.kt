package com.example.project.ui.cart

import android.app.Activity
import android.app.Application
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.CreateDocument
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.project.data.IItemsRepo
import com.example.project.data.impl.ItemsDatabaseRepository
import com.example.project.data.model.Item
import com.example.project.network.IItemsFetcher
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class CartListViewModel(
                        app: Application,
                        private val repository: IItemsRepo
) : AndroidViewModel(app) {
    // Mutable state holding all items in the repository
    private val _items: MutableState<List<Item>> = mutableStateOf(listOf())
    val items: State<List<Item>> = _items

    // holds a Float for the total balance of items
    private val _totalAmount: MutableState<Float> = mutableStateOf(0f)
    val totalAmount: State<Float> = _totalAmount

    // Holds the local repository of items (cart)
    private val _repository: IItemsRepo = repository

    init {
        viewModelScope.launch {
            _items.value = _repository.getItems() // store all items in the repository

            // re-calculate the total balance of the items
            var sum = 0f
            for (item in _items.value) {
                val price = item.price.removePrefix("$ ").toFloatOrNull() ?: 0f
                val amount = item.amount.toFloatOrNull() ?: 0f
                sum += price * amount
            }
            _totalAmount.value = sum
        }
    }

    fun deleteItem(item: Item) {
        viewModelScope.launch {
            _repository.deleteItem(item) // Delete item from the database
            _items.value = _repository.getItems() // Refresh the list after deletion

            // re-calculate total balance of items in cart
            var sum = 0f
            for (item in _items.value) {
                val price = item.price.removePrefix("$ ").toFloatOrNull() ?: 0f
                val amount = item.amount.toFloatOrNull() ?: 0f
                sum += price * amount
            }
            _totalAmount.value = sum
        }
    }

    fun updateItem(item: Item, amount: String) {
        viewModelScope.launch {
            item.amount = amount
            repository.updateItem(item, amount) // Update item amount in the database
            _items.value = repository.getItems() // Refresh the list after update

            // re-calculate total balance of items in cart
            var sum = 0f
            for (item in _items.value) {
                val price = item.price.removePrefix("$ ").toFloatOrNull() ?: 0f
                val amount = item.amount.toFloatOrNull() ?: 0f
                sum += price * amount
            }
            _totalAmount.value = sum
        }
    }

    // function to handle document creation and saving items in cart as shopping list
    fun createNoteAsDocument(context: Context, shoppingList: String) {
        // create an intent for text document creation
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "text/plain"
            putExtra(Intent.EXTRA_TITLE, "ShoppingList.txt") // Set the default file name
            flags = Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        }

        try {
            // Attempt to launch the document creation activity using activityResultRegistry
            (context as? ComponentActivity)?.activityResultRegistry?.let { registry ->
                val launcher = registry.register("key", ActivityResultContracts.StartActivityForResult()) { result ->
                    // Check if the result is OK and get URI from the result
                    if (result.resultCode == Activity.RESULT_OK) {
                        result.data?.data?.let { uri ->
                            try {
                                // Write the shopping list content to the document using the URI
                                context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                                    outputStream.write(shoppingList.toByteArray())
                                    Log.d(TAG, "Shopping list saved successfully")
                                }
                            } catch (e: IOException) {
                                // Handle any IO exception
                                Log.e(TAG, "Error writing to document: ${e.message}")
                            }
                        }
                    } else {
                        // Log if the document creation was canceled by the user
                        Log.d(TAG, "Document creation canceled by user")
                    }
                }
                // Launch the activity for document creation
                launcher.launch(intent)
            }
        } catch (e: Exception) {
            // Handle any other exception
            Log.e("createNoteAsDocument", "Error creating document: ${e.message}")
        }
    }
}