package com.jeklov.legalentities.data.view.model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jeklov.legalentities.data.repository.AuthorizationRepository
import com.jeklov.legalentities.data.repository.WebsocketRepository

//class WebsocketViewModelProviderFactory(private val userViewModel: UserViewModel) : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(WebsocketViewModel::class.java)) {
//            @Suppress("UNCHECKED_CAST")
//            return WebsocketViewModel(userViewModel) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}
class WebsocketViewModelProviderFactory(val application: Application, private val repository: WebsocketRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WebsocketViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WebsocketViewModel(application, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}