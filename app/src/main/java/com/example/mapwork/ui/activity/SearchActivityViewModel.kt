package com.example.mapwork.ui.activity

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mapwork.base.BaseViewModel
import com.example.mapwork.base.Event
import com.example.mapwork.models.request.PropertyByPincodeRequest
import com.example.mapwork.models.response.NetworkResult
import com.example.mapwork.models.response.PropertyDataResponse
import com.example.mapwork.models.response.RecentSearch
import kotlinx.coroutines.launch

class SearchActivityViewModel(application: Application) : BaseViewModel(application) {

    val propertyDataResponse get() = repository.propertyDataResponse

    private val _recentSearchResponse =
        MutableLiveData<Event<NetworkResult<ArrayList<RecentSearch>>>>()
    val recentSearchResponse get() = _recentSearchResponse

    init {
        addReccentSearch()
    }

    fun getPropertyByPincode(request: PropertyByPincodeRequest) {
        Log.d("Request", gson.toJson(request))
        viewModelScope.launch {
            repository.getPropertyByPincode(request)
        }
    }

    fun addReccentSearch() {
        var recentsearch_list = arrayListOf<RecentSearch>()
        recentsearch_list.add(RecentSearch("0", "Search near by","20A bani park 302017","4.2 Km", "26.880640", "75.810930"))
        recentsearch_list.add(RecentSearch("48", "Single room","C6 malviya nagar jaipur 302017","1.2 KM", "26.880640", "75.810930"))
        recentsearch_list.add(RecentSearch("49", "Single room", "pno 203  malviya nagar jaipur","0.9 KM","26.880335", "75.810904"))
        recentsearch_list.add(RecentSearch("48", "Double room", "24D apex circle 302015","2.2 KM","26.880640", "75.810930"))
        recentsearch_list.add(RecentSearch("49", "Basement", "330 tilak nagar 302020","1.2 KM","26.880335", "75.810904"))
        recentsearch_list.add(RecentSearch("49", "Room + Kithen", "120 tilak nagar 302020","1.2 KM","26.880335", "75.810904"))
        recentsearch_list.add(RecentSearch("49", "1 BHK", "50 Bajaj nagar 302020","1.2 KM","26.880335", "75.810904"))
        recentsearch_list.add(RecentSearch("49", "Single Room", "05D Barkat nagar 302020","4.2 KM","26.880335", "75.810904"))
        recentsearch_list.add(RecentSearch("49", "Room Only", "330 tilak nagar 302020","5.2 KM","26.880335", "75.810904"))
        _recentSearchResponse.postValue(Event(NetworkResult.Success(recentsearch_list)))

    }


}