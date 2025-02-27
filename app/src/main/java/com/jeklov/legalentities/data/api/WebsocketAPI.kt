package com.jeklov.legalentities.data.api

import com.jeklov.legalentities.data.models.Profile
import com.jeklov.legalentities.data.models.ProfileRequest
import com.jeklov.legalentities.data.models.WebsocketRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface WebsocketAPI {
    @POST("chat/chatrooms/")
    suspend fun getWebsocket(
        @Body request: String
    ): Response<WebsocketRequest>
}