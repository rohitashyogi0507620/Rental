package com.example.mapwork.adapter

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.mapwork.R
import com.example.mapwork.databinding.LayoutRoominfoShortBinding
import com.example.mapwork.models.response.MakerInfoData
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.google.gson.Gson


class InfoWindowAdapter(mContext: Context) : GoogleMap.InfoWindowAdapter {


    var mWindow: View = LayoutInflater.from(mContext).inflate(R.layout.layout_maker_info, null)

    override fun getInfoContents(marker: Marker): View? {
        return null
    }

    override fun getInfoWindow(marker: Marker): View? {

        val gson = Gson()
        val aMarkerInfo: MakerInfoData = gson.fromJson(marker.snippet, MakerInfoData::class.java)

        val tvName = mWindow.findViewById<TextView>(R.id.room_makername)
        val tvLocation = mWindow.findViewById<TextView>(R.id.room_makerlocation)
        val tvPrice = mWindow.findViewById<TextView>(R.id.room_makerprice)
        tvName.text = aMarkerInfo.title
        tvLocation.text = aMarkerInfo.subtitle
        tvPrice.text = aMarkerInfo.price
        return mWindow
    }
}