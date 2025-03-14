package com.dev.quickcart.data.model

import androidx.room.PrimaryKey

data class User(

    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val username: String,
    val phoneNumber: String,

)
