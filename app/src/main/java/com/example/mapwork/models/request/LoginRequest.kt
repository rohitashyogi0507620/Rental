package com.example.mapwork.models.request

data class LoginRequest(
    val api_key: String,
    val password: String,
    val type: String,
    val username: String
)