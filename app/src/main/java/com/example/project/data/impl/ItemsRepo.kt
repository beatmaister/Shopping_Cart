package com.example.project.data.impl

import com.example.project.data.IItemsRepo
import com.example.project.data.model.Item

class ItemsRepo : IItemsRepo {

    // initialize with 20 example items
    private var original = (0..20).map { i ->
        Item(i.toString(), "Item $i", "Description $i", "$123", "1", i % 3 == 0)
    }

    // store example all items in list variable
    private var items = original.filter { true }

    // function to get the current list of items
    override suspend fun getItems(): List<Item> {
        return items
    }

    // function to delete an items from the list by filtering using IDs
    override suspend fun deleteItem(item: Item) {
        items = items.filter { i -> i.id != item.id }

    }

    // Update item amounts by
    // creating a copy, changing the amount value, and replacing using its ID
    override suspend fun updateItem(item: Item, amount: String) {
        val updatedItem = item.copy(amount = amount)
        items = items.map { if (it.id == item.id) updatedItem else it }
    }

    // add/ append an item to the current list/database
    override suspend fun addItem(item: Item) {
        items = items + item
    }


}