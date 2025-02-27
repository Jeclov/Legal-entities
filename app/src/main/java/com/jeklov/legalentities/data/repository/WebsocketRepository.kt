package com.jeklov.legalentities.data.repository

import com.jeklov.legalentities.data.api.RetrofitInstance

class WebsocketRepository {

    suspend fun getWebsocket(login: String) =
        RetrofitInstance.websocketAPI.getWebsocket(login)


}