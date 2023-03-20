package com.example.mapwork.api

import com.google.android.gms.common.api.Api

object APIConstant {


    const val BASE_URL = "http://10.10.8.63:3000/"

    //192.168.0.99:3000/routes/v1/searchproperty/pincode
    const val API_KEY = "db32c67d-1cbf-4154-b71d-8458659dbc49"

    const val STATUS_SUCCESS = 200
    const val STATUS_ERROR = 404
    const val STATUS_SERVERERROR = 500


    const val DEVICE_ID = "ANDROID"
    const val FCM_TOKEN = "ANDROID"
    const val API = "routes/"
    const val API_VERSION = "v1/"
    const val SEARCH_PROPERTY = "searchproperty/"
    const val PINCODE = "pincode"
    const val PROPERTY_BYID = "propertybyid"


    const val USER_REGISTER = API + "post_user_register/"
    const val USER_LOGIN = API + "get_user_login/"


    const val SEARCH_PROPERTY_BY_PINCODE = API + API_VERSION + SEARCH_PROPERTY + PINCODE
    const val PROPERTY_DETAILS_BY_ID = API + API_VERSION + SEARCH_PROPERTY + PROPERTY_BYID



}