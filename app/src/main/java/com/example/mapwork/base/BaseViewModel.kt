package com.example.mapwork.base

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mapwork.di.APIClient
import com.example.mapwork.models.response.LoginResponse
import com.example.mapwork.repositroy.AppRepository
import com.example.mapwork.utils.SessionConstants
import com.example.mapwork.utils.SessionManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

open class BaseViewModel(application: Application) : AndroidViewModel(application) {

    var repository: AppRepository
    var context: Context
    var sessionManager: SessionManager
    var gson: Gson

    var _userlogin: LoginResponse?=null
    val userLoginResponse get() = _userlogin!!


    var isUserLogin: Boolean

    val internetConnection: LiveData<Event<Boolean>>
        get() = repository.internetConnection

    private val _progressLayout = MutableLiveData<Boolean>()
    val progressLayout get() = _progressLayout


    init {
        context = getApplication<Application>().applicationContext
        repository = AppRepository(APIClient, context)
        gson = Gson()
        sessionManager = SessionManager()
        sessionManager.setContext(SessionConstants.APPLICATION_STATE, context)

        isUserLogin=sessionManager.getBooleanData(SessionConstants.IS_LOGIN)

        if (isUserLogin){
            _userlogin = gson.fromJson(
                sessionManager.getData(SessionConstants.LOGIN_RESPONSE),
                object : TypeToken<LoginResponse>() {}.type
            )
        }else{
            _userlogin=null
        }

    }
}