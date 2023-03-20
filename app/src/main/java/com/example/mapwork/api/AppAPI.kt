package com.example.mapwork.api

import com.example.mapwork.models.request.LoginRequest
import com.example.mapwork.models.request.PropertyByPincodeRequest
import com.example.mapwork.models.request.PropertyDetailsByIdRequest
import com.example.mapwork.models.request.RegisterRequest
import com.example.mapwork.models.response.BaseResponse
import com.example.mapwork.models.response.LoginResponse
import com.example.mapwork.models.response.PropertyDataResponse
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

    @POST(APIConstant.SEARCH_PROPERTY_BY_PINCODE)
    suspend fun getPropertyByPincode(@Body registerRequest: PropertyByPincodeRequest): Response<BaseResponse<List<PropertyDataResponse>>>

    @POST(APIConstant.PROPERTY_DETAILS_BY_ID)
    suspend fun getPropertyDetailsById(@Body registerRequest: PropertyDetailsByIdRequest): Response<BaseResponse<PropertyDataResponse>>


}
