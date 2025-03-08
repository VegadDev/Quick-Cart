package com.dev.quickcart.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "product_table")
data class Product(

    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val prodName: String,
    val prodImage: ByteArray,
    val prodPrice: String,
    val prodDescription: String = "",
    val prodCalories: String = "",
    val prodProtein: String = "",
    val prodFat: String = "",
    val prodFiber: String = "",
    val lastModified: Long
)
