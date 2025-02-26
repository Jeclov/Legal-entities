package com.jeklov.legalentities.data.models

data class Profile(
    //val id: Int? = null, // DataBase generated
    val login: String,
    var password: String,
    val email: String,
    var token: String? = null,
    val role: String
)