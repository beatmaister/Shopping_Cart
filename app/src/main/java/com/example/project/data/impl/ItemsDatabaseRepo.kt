package com.example.project.data.impl

import android.app.Application
import androidx.room.Room
import com.example.project.data.IItemsRepo
import com.example.project.data.ItemsDatabase
import com.example.project.data.model.Item
class ItemsDatabaseRepository(app: Application) : IItemsRepo {

    // storing the database as a variable
    private val db: ItemsDatabase

    init {
        // build the database using Room
        db = Room.databaseBuilder(app, ItemsDatabase::class.java, "songs.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    // calling each function in ItemsRepo
    override suspend fun getItems(): List<Item> {
        return db.itemsDao().getItems()
    }

    override suspend fun deleteItem(item: Item) {
        db.itemsDao().deleteItem(item)
    }

    override suspend fun updateItem(item: Item, amount: String) {
        db.itemsDao().updateItem(item)
    }

    override suspend fun addItem(item: Item) {
        db.itemsDao().addItem(item)
    }

}