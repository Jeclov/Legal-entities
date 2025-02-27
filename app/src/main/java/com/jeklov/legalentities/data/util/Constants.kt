package com.jeklov.legalentities.data.util

class Constants {
    companion object{
        const val BASE_URL = "http://185.239.50.182:10000/"
        const val BASE_URL_WEBSOCKET = "ws://localhost:10000/ws/chat/"

        const val NO_INTERNET_CONNECTION = "No internet connection"
        const val UNABLE_TO_CONNECT = "Unable to connect"
        const val NO_SIGNAL = "No signal"
        const val TOO_MANY_REQUESTS = "Too Many Requests"
        const val INVALID_SERVER_REQUESTS = "Invalid server requests"

        const val ROLE_SUPERUSER = "superuser"
        const val ROLE_STAFF = "staff"
        const val ROLE_CLIENT = "client"
    }
}