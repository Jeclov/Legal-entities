package com.jeklov.legalentities.data.repository

import com.jeklov.legalentities.data.api.RetrofitInstance
import com.jeklov.legalentities.data.models.LoginAndPassword
import com.jeklov.legalentities.data.models.ProfileRequest

class AuthorizationRepository {

    suspend fun login(login: String, password: String) =
        RetrofitInstance.profileAPI.login(
            LoginAndPassword(
                login, password
            )
        )

    suspend fun signup(login: String, password: String, email: String) =
        RetrofitInstance.profileAPI.signup(
            ProfileRequest(
                login, password, email
            )
        )
}