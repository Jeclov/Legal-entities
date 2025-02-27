package com.jeklov.legalentities.data.view.model

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeklov.legalentities.data.models.Message
import com.jeklov.legalentities.data.models.TokenRequest
import com.jeklov.legalentities.data.models.User
import com.jeklov.legalentities.data.models.WebsocketRequest
import com.jeklov.legalentities.data.repository.AuthorizationRepository
import com.jeklov.legalentities.data.repository.WebsocketRepository
import com.jeklov.legalentities.data.util.Constants.Companion.INVALID_SERVER_REQUESTS
import com.jeklov.legalentities.data.util.Constants.Companion.NO_INTERNET_CONNECTION
import com.jeklov.legalentities.data.util.Constants.Companion.NO_SIGNAL
import com.jeklov.legalentities.data.util.Resource
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

@OptIn(DelicateCoroutinesApi::class)
class WebsocketViewModel(
    app: Application,
    private val repository: WebsocketRepository
) : AndroidViewModel(app) {

    //    private val _socketStatus = MutableLiveData(false)
//    val socketStatus: LiveData<Boolean> = _socketStatus
//
//    private val _messageBody = MutableLiveData<Message>()
//    val messageBody: LiveData<Message> = _messageBody
//
//    fun setStatus(status: Boolean) = GlobalScope.launch(Dispatchers.Main) {
//        _socketStatus.value = status
//    }
//
//    fun setMessage(message: Message) = GlobalScope.launch(Dispatchers.Main) {
//
//        if (_socketStatus.value == true) {
//            _messageBody.value = message
//        }
//    }
    val websocketData: MutableLiveData<Resource<WebsocketRequest>> = MutableLiveData()

    fun getWebsocketData(login: String): WebsocketRequest? {
        var res: WebsocketRequest? = null
        viewModelScope.launch {
            res = websocketInternet(login)
        }
        return res
    }

    private var websocketResponse: WebsocketRequest? = null

    private fun internetConnection(context: Context): Boolean {
        (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).apply {
            return getNetworkCapabilities(activeNetwork)?.run {
                when {
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            } ?: false
        }
    }

    private fun websocketResponse(response: Response<WebsocketRequest>): Resource<WebsocketRequest> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                websocketResponse = resultResponse
                if (resultResponse.chatRoomId != "null") {
                    return Resource.Success(resultResponse)
                } else {
                    return Resource.Error(INVALID_SERVER_REQUESTS)
                }
            }
        }
        return Resource.Error(response.message())
    }

    private suspend fun websocketInternet(login: String): WebsocketRequest? {
        websocketData.postValue(Resource.Loading)
        try {
            if (internetConnection((this.getApplication()))) {
                val response = repository.getWebsocket(login)
                websocketData.postValue(websocketResponse(response))
                return if (response.isSuccessful) response.body() else null
            } else {
                websocketData.postValue(Resource.Error(NO_INTERNET_CONNECTION))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> websocketData.postValue(Resource.Error(t.message.toString()))
                else -> websocketData.postValue(Resource.Error(NO_SIGNAL))
            }
        }
        return null
    }
}