package com.example.mapwork

import RecentSearchAdapter
import android.location.Address
import android.location.Geocoder
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mapwork.base.BaseActivity
import com.example.mapwork.databinding.ActivitySearchBinding
import com.example.mapwork.models.response.NetworkResult
import com.example.mapwork.models.response.RecentSearch
import com.example.mapwork.ui.activity.SearchActivityViewModel
import `in`.probusinsurance.probusdesign.ui.dialog.AlertDialog
import java.io.IOException


class SearchActivity : BaseActivity<ActivitySearchBinding, SearchActivityViewModel>(),
    RecentSearchAdapter.OnItemClickListener {

    var recentsearch_list = mutableListOf<RecentSearch>()
    lateinit var adapterRecentSearch: RecentSearchAdapter

    override fun getLayoutId(): Int {
        return R.layout.activity_search
    }

    override fun buildViewModel(): SearchActivityViewModel {
        return ViewModelProvider(this).get(SearchActivityViewModel::class.java)
    }

    override fun getBundle() {
    }

    override fun initViews() {

        binding.searchRecentRecyclearView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapterRecentSearch = RecentSearchAdapter(applicationContext, recentsearch_list, this)
        binding.searchRecentRecyclearView.adapter = adapterRecentSearch


        binding.homeTxtsearchview.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                //searchLocation(binding.homeTxtsearchview.text.toString())
            }
        })

    }

    override fun initLiveDataObservers() {

        viewModel.recentSearchResponse.observe(this) {
            it.getContentIfNotHandled()?.let {
                //binding.progressLayout.setShowProgress(false)

                when (it) {
                    is NetworkResult.Success -> {
                        recentsearch_list.addAll(it.data!!)
                        adapterRecentSearch.notifDataChanged()

                    }
                    is NetworkResult.Error -> {
                        AlertDialog.ErrorDialog(this, it.errrormessage.toString())
                    }
                    is NetworkResult.Loading -> {
                        // binding.progressLayout.setShowProgress(true)

                    }
                    else -> {

                    }
                }
            }
        }

    }

    override fun onClickListener() {

    }

    override fun onItemClick(item: RecentSearch, position: Int, type: Int) {
        Toast.makeText(applicationContext, item.toString(), Toast.LENGTH_SHORT)
    }

    fun searchLocation(location: String): List<Address>? {

        var addressList: List<Address>? = null
        if (location != null || location != "") {
            val geocoder = Geocoder(this)
            try {
                addressList = geocoder.getFromLocationName(location, 5)
                Log.d("LISTADDRESS",addressList.toString())
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return addressList
    }

}