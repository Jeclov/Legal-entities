package com.jeklov.legalentities.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jeklov.legalentities.data.models.Message
import com.jeklov.legalentities.data.models.User
import java.util.Date

internal class Converters {

    private val gson: Gson = GsonBuilder().create()

    // Converter for Date
    @TypeConverter
    fun fromDate(value: Date?): Long? {
        return value?.time
    }

    @TypeConverter
    fun toDate(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    // Converter for List<String>
    @TypeConverter
    fun fromStringList(value: List<String>?): String? {
        return value?.joinToString(separator = ",")
    }

    @TypeConverter
    fun toStringList(value: String?): List<String> {
        return value?.split(",") ?: emptyList()
    }

    // Converter for List<User>
    @TypeConverter
    fun fromUserList(value: List<User>?): String? {
        return value?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toUserList(value: String?): List<User>? {
        return value?.let { gson.fromJson(it, Array<User>::class.java).toList() }
    }

    // Converter for User
    @TypeConverter
    fun fromUser(value: User?): String? {
        return value?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toUser(value: String?): User? {
        return value?.let { gson.fromJson(it, User::class.java) }
    }

    // Converter for List<Message>
    @TypeConverter
    fun fromMessageList(value: List<Message>?): String? {
        return value?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toMessageList(value: String?): List<Message>? {
        return value?.let { gson.fromJson(it, Array<Message>::class.java).toList() }
    }

    // Converter for List<Any>
    @TypeConverter
    fun fromAnyList(value: List<Any>?): String? {
        return value?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toAnyList(value: String?): List<Any>? {
        return value?.let { gson.fromJson(it, Array<Any>::class.java).toList() }
    }
}