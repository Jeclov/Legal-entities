package com.jeklov.legalentities.data.view.model

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jeklov.legalentities.data.models.Profile
import com.jeklov.legalentities.data.models.TokenRequest
import com.jeklov.legalentities.data.repository.AuthorizationRepository
import com.jeklov.legalentities.data.util.Constants.Companion.INVALID_SERVER_REQUESTS
import com.jeklov.legalentities.data.util.Constants.Companion.NO_INTERNET_CONNECTION
import com.jeklov.legalentities.data.util.Constants.Companion.NO_SIGNAL
import com.jeklov.legalentities.data.util.Constants.Companion.UNABLE_TO_CONNECT
import com.jeklov.legalentities.data.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class AuthorizationViewModel(
    app: Application,
    private val repository: AuthorizationRepository
) : AndroidViewModel(app) {

//    private val _isFinished = MutableLiveData<Boolean>()
//    val isFinished: LiveData<Boolean>
//        get() = _isFinished
//    private val delay = 3000L
//
//    init {
//        _isFinished.value = false
//        Handler(Looper.getMainLooper()).postDelayed({
//            _isFinished.value = true
//        }, delay)
//    }

    val loginToken: MutableLiveData<Resource<TokenRequest>> = MutableLiveData()

    private var loginResponseToken: TokenRequest? = null

    val signupData: MutableLiveData<Resource<Profile>> = MutableLiveData()

    private var signupResponseProfile: Profile? = null

    fun login(login: String, password: String) =
        viewModelScope.launch {
            loginInternet(login, password)
        }

    fun signup(login: String, password: String, email: String) =
        viewModelScope.launch {
            signupInternet(login, password, email)
        }

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

    private fun loginResponse(response: Response<TokenRequest>): Resource<TokenRequest> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                loginResponseToken = resultResponse
                if (resultResponse.token != "null" &&
                    resultResponse.role != "null"
                ) {
                    return Resource.Success(resultResponse)
                } else {
                    return Resource.Error(INVALID_SERVER_REQUESTS)
                }
            }
        }
        return Resource.Error(response.message())
    }

    private fun signupResponse(response: Response<Profile>, password: String): Resource<Profile> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                signupResponseProfile = resultResponse
                signupResponseProfile!!.password = password
                if (resultResponse.login != "null" &&
                    resultResponse.email != "null" &&
                    resultResponse.role != "null" &&
                    resultResponse.token != "null"
                ) {
                    return Resource.Success(signupResponseProfile ?: resultResponse)
                } else {
                    return Resource.Error(INVALID_SERVER_REQUESTS)
                }
            }
        }
        return Resource.Error(response.message())
    }

    private suspend fun loginInternet(login: String, password: String) {
        loginToken.postValue(Resource.Loading)
        try {
            if (internetConnection((this.getApplication()))) {
                val response = repository.login(login, password)
                loginToken.postValue(loginResponse(response))
            } else {
                loginToken.postValue(Resource.Error(NO_INTERNET_CONNECTION))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> loginToken.postValue(Resource.Error(t.message.toString()))
                else -> loginToken.postValue(Resource.Error(NO_SIGNAL))
            }
        }
    }

    private suspend fun signupInternet(login: String, password: String, email: String) {
        signupData.postValue(Resource.Loading)
        try {
            if (internetConnection((this.getApplication()))) {
                val response = repository.signup(login, password, email)
                signupData.postValue(signupResponse(response, password))
            } else {
                signupData.postValue(Resource.Error(NO_INTERNET_CONNECTION))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> signupData.postValue(Resource.Error(t.message.toString()))
                else -> signupData.postValue(Resource.Error(NO_SIGNAL))
            }
        }
    }
}