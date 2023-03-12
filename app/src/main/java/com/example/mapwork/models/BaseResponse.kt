package com.example.mapwork.models.response

data class BaseResponse<T>(
    var result: T,
    var StatusCode: Int
)