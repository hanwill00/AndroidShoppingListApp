package com.example.shoppinglistassignment2.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "shopping_item")
data class ShoppingItem (
    @PrimaryKey(autoGenerate = true) var shoppingItemId: Long?,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "category") var category: Int,

    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "price") var price: Int,
    @ColumnInfo(name = "status") var status: Boolean
) : Serializable


