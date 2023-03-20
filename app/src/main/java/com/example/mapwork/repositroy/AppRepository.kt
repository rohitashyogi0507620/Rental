package com.example.mapwork.repositroy

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.mapwork.api.APIConstant.STATUS_SUCCESS
import com.example.mapwork.base.Event
import com.example.mapwork.di.APIClient
import com.example.mapwork.models.request.LoginRequest
import com.example.mapwork.models.request.PropertyByPincodeRequest
import com.example.mapwork.models.request.PropertyDetailsByIdRequest
import com.example.mapwork.models.request.RegisterRequest
import com.example.mapwork.models.response.LoginResponse
import com.example.mapwork.models.response.NetworkResult
import com.example.mapwork.models.response.PropertyDataResponse
import com.example.mapwork.models.response.RegisterResponse
import com.example.mapwork.utils.Utils


class AppRepository constructor(val apiClient: APIClient, val applicationContext: Context) {


    private val _internetConnection = MutableLiveData<Event<Boolean>>()
    val internetConnection get() = _internetConnection

    private val _loginUserresponse = MutableLiveData<Event<NetworkResult<LoginResponse>>>()
    val loginUserresponse get() = _loginUserresponse

    private val _registerUserResponse = MutableLiveData<Event<NetworkResult<RegisterResponse>>>()
    val registerUserResponse get() = _registerUserResponse


    private val _propertyDataResponse =
        MutableLiveData<Event<NetworkResult<List<PropertyDataResponse>>>>()
    val propertyDataResponse get() = _propertyDataResponse

    private val _propertyDetailsByIdResponse =
        MutableLiveData<Event<NetworkResult<PropertyDataResponse>>>()
    val propertyDetailsByIdResponse get() = _propertyDetailsByIdResponse


    init {
        _internetConnection.postValue(Event(Utils.isOnline(applicationContext)))
    }

    suspend fun userLogin(request: LoginRequest) {

        _loginUserresponse.postValue(Event(NetworkResult.Loading()))
        val response = apiClient.apiService.loginUser(request)
        if (response.isSuccessful && response.body() != null && response.body()!!.result != null) {
            if (response.body()!!.result.size > 0) {
                var response = response.body()!!.result.get(0)
                if (response.success.equals("1")) {
                    _loginUserresponse.postValue(Event(NetworkResult.Success(response)))
                } else if (response.success.equals("0") && response.msg != null) {
                    _loginUserresponse.postValue(Event(NetworkResult.Error(response.msg)))
                }
            } else {
                _loginUserresponse.postValue(Event(NetworkResult.Error("Something Went Wrong")))
            }
        } else {
            _loginUserresponse.postValue(Event(NetworkResult.Error("Something Went Wrong")))
        }
    }

    suspend fun userRegister(request: RegisterRequest) {

        _registerUserResponse.postValue(Event(NetworkResult.Loading()))
        val response = apiClient.apiService.registerUser(request)
        if (response.isSuccessful && response.body() != null && response.body()!!.result != null) {
            if (response.body()!!.result.size > 0) {
                var response = response.body()!!.result.get(0)
                if (response.success.equals("1")) {
                    _registerUserResponse.postValue(Event(NetworkResult.Success(response)))
                } else if (response.success.equals("0") && response.msg != null) {
                    // _registerUserResponse.postValue(Event(NetworkResult.Error(response.msg)))
                    _registerUserResponse.postValue(Event(NetworkResult.Error("Something Went Wrong")))

                }
            } else {
                _registerUserResponse.postValue(Event(NetworkResult.Error("Something Went Wrong")))
            }
        } else {
            _registerUserResponse.postValue(Event(NetworkResult.Error("Something Went Wrong")))
        }
    }

    suspend fun getPropertyByPincode(request: PropertyByPincodeRequest) {

        _loginUserresponse.postValue(Event(NetworkResult.Loading()))
        val response = apiClient.apiService.getPropertyByPincode(request)
        if (response.isSuccessful && response.body() != null) {
            if (response.body()!!.statusCode.equals(STATUS_SUCCESS)) {
                _propertyDataResponse.postValue(Event(NetworkResult.Success(response.body()!!.result)))
            } else {
                _propertyDataResponse.postValue(Event(NetworkResult.Error(response.body()!!.message)))
            }
        } else {
            _propertyDataResponse.postValue(Event(NetworkResult.Error("Something Went Wrong")))
        }
    }

    suspend fun getPropertyDetailsById(request: PropertyDetailsByIdRequest) {

        _loginUserresponse.postValue(Event(NetworkResult.Loading()))
        val response = apiClient.apiService.getPropertyDetailsById(request)
        if (response.isSuccessful && response.body() != null) {
            if (response.body()!!.statusCode.equals(STATUS_SUCCESS)) {
                _propertyDetailsByIdResponse.postValue(Event(NetworkResult.Success(response.body()!!.result)))
            } else {
                _propertyDetailsByIdResponse.postValue(Event(NetworkResult.Error(response.body()!!.message)))
            }
        } else {
            _propertyDetailsByIdResponse.postValue(Event(NetworkResult.Error("Something Went Wrong")))
        }
    }


}