//package com.jeklov.legalentities.data.repository
//
//import com.jeklov.legalentities.data.util.Resource
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.withContext
//import retrofit2.HttpException
//
//abstract class ProfileRepository {
//    suspend fun <T> retrofitApiCall(apiCall: suspend () -> T): Resource<T> {
//        return withContext(Dispatchers.IO) {
//            try {
//                Resource.Success(apiCall.invoke())
//            } catch (throwable: Throwable) {
//                when(throwable) {
//                    is HttpException -> {
//                        Resource.Failure(false, throwable.code(), throwable.response()?.errorBody())
//                    } else -> Resource.Failure(false, null, null)
//                }
//            }
//        }
//    }
//}