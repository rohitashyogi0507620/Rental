package com.example.mapwork.models.response

data class BaseResponse<T>(
    var result: T,
    var statusCode: Int,
    var status: String,
    var message: String
)