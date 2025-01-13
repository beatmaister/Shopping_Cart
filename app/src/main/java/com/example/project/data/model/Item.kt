package com.example.project.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

// Specifying each Items fields within the database schema using Room
@Entity(tableName = "items")
data class Item (
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String,
    val price: String,
    var amount: String,
    @SerializedName("is_available")
    val isAvailable: Boolean
) {
}