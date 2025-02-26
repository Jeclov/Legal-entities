package com.jeklov.legalentities.data.models

data class ProfileRequest(
    val login: String,
    val password: String,
    val email: String
)