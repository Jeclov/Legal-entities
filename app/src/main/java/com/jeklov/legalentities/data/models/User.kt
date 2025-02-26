package com.jeklov.legalentities.data.models

import androidx.annotation.DrawableRes
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "userItems")
data class User(
    @PrimaryKey
    val id: Int? = null, // DataBase generated
    val login: String,
    val email: String,
    @DrawableRes
    val iconRes: Int? = null,
    var messageList: List<Message>? = null
)
