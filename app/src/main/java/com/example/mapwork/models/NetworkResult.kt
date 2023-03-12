package com.example.mapwork.models.response

sealed class NetworkResult<T>(val data:T? =null, val errrormessage:String?=null) {
    class Loading<T>:NetworkResult<T>()
    class Success<T>(data: T?=null):NetworkResult<T>(data=data)
    class Error<T>(message: String):NetworkResult<T>(errrormessage = message)
}