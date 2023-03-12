package com.example.mapwork.api

import com.example.mapwork.models.request.LoginRequest
import com.example.mapwork.models.request.RegisterRequest
import com.example.mapwork.models.response.BaseResponse
import com.example.mapwork.models.response.LoginResponse
import com.example.mapwork.models.response.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AppAPI {

    @POST(APIConstant.USER_REGISTER)
    suspend fun registerUser(@Body registerRequest: RegisterRequest): Response<BaseResponse<List<RegisterResponse>>>

    @POST(APIConstant.USER_LOGIN)
    suspend fun loginUser(@Body registerRequest: LoginRequest): Response<BaseResponse<List<LoginResponse>>>

}