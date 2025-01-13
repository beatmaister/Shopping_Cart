package com.example.project.data

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Update
import com.example.project.data.model.Item

@Dao
interface ItemsDao {
    @Query("select * from items")
    suspend fun getItems(): List<Item> // function to get item list from the database

    @Insert
    suspend fun addItem(song: Item) // function to add an item to the database

    @Delete
    suspend fun deleteItem(song: Item) // function to delete an item from the database

    @Update
    suspend fun updateItem(item: Item) // function to update an item's fields within the database

}

@Database(entities = [Item::class], version = 8, exportSchema = true)
abstract class ItemsDatabase : RoomDatabase() {
    // use Room to create a database schema
    abstract fun itemsDao(): ItemsDao
}