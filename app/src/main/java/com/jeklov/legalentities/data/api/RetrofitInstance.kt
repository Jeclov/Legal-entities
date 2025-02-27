package com.jeklov.legalentities.data.api

import com.jeklov.legalentities.data.util.Constants.Companion.BASE_URL
import com.jeklov.legalentities.data.util.Constants.Companion.BASE_URL_WEBSOCKET
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {
    companion object {

        private val retrofitProfile by lazy {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        val profileAPI by lazy {
            retrofitProfile.create(ProfileAPI::class.java)
        }

        private val retrofitWebsocket by lazy {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        val websocketAPI by lazy {
            retrofitWebsocket.create(WebsocketAPI::class.java)
        }
    }
}