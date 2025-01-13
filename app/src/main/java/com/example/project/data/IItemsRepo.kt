package com.example.project.data
import com.example.project.data.model.Item


interface IItemsRepo{
    suspend fun getItems(): List<Item> // function to get item list from the database
    suspend fun deleteItem(item: Item) // function to delete an item from the database
    suspend fun updateItem(item: Item, amount: String) // function to update an item's fields within the database
    suspend fun addItem(item: Item) // function to add an item to the database

}
