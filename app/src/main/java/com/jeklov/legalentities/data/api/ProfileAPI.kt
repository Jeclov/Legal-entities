package com.jeklov.legalentities.data.api

import com.jeklov.legalentities.data.models.LoginAndPassword
import com.jeklov.legalentities.data.models.Profile
import com.jeklov.legalentities.data.models.ProfileRequest
import com.jeklov.legalentities.data.models.TokenRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ProfileAPI {

    @POST("auth/login/")
    suspend fun login(
        @Body request: LoginAndPassword
    ): Response<TokenRequest>

    @POST("auth/")
    suspend fun signup(
        @Body request: ProfileRequest
    ): Response<Profile>
}