package com.jeklov.legalentities.data.view.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeklov.legalentities.data.db.MainDB
import com.jeklov.legalentities.data.models.Message
import com.jeklov.legalentities.data.models.User
import kotlinx.coroutines.launch

class UserViewModel(private val database: MainDB) : ViewModel() {

    val allUsers: LiveData<List<User>> =
        database.userDao().getAllUserItems()

    fun upsertUser(user: User) {
        viewModelScope.launch {
            database.userDao().upsertUserItem(user)
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch {
            database.userDao().deleteUserItem(user)
        }
    }

    fun getUserById(id: Int?) =
        if (id != null) database.userDao().getUserByIdItems(id) else null

    fun setMessage(user: User, message: Message) {
        if (user.messageList == null) {
            user.messageList = listOf(message)
        } else {
            user.messageList = user.messageList!! + message
        }
        upsertUser(user)
    }
}