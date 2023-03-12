package com.example.mapwork.models.response

data class LoginResponse(
    val country_code: String,
    val email: String,
    val full_name: String,
    val id: String,
    val mobile: String,
    val msg: String,
    val profile_img: String,
    val success: String,
    val username: String,
    val whatsapp_no: String
)