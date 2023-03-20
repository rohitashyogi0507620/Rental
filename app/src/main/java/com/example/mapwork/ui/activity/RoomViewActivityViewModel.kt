package com.example.mapwork.ui.activity
import android.app.Application
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.mapwork.base.BaseViewModel
import com.example.mapwork.models.request.PropertyByPincodeRequest
import com.example.mapwork.models.request.PropertyDetailsByIdRequest
import kotlinx.coroutines.launch

class RoomViewActivityViewModel(application: Application) : BaseViewModel(application) {

    val propertyDetailsByIdResponse get() = repository.propertyDetailsByIdResponse
    init {

    }

    fun getPropertyDetailsById(request: PropertyDetailsByIdRequest) {
        Log.d("Request",gson.toJson(request))
        viewModelScope .launch {
            repository.getPropertyDetailsById(request)
        }
    }


}