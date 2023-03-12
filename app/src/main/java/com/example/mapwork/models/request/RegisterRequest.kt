package com.example.mapwork.models.request

data class RegisterRequest(
    var CATEGORY_EMAIL: String?=null,
    var api_key: String?=null,
    var country_code: String?=null,
    var device_id: String?=null,
    var fcm_token: String?=null,
    var full_name: String?=null,
    var mobile: String?=null,
    var password: String?=null,
    var referer: String?=null,
    var username: String?=null
)