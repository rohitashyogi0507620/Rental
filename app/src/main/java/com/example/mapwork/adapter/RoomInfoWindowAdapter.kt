package com.example.mapwork.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.example.mapwork.R
import com.example.mapwork.models.response.RoomData
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class RoomInfoWindowAdapter(val context: Context) : GoogleMap.InfoWindowAdapter {

    override fun getInfoContents(marker: Marker): View? {
        // 1. Get tag
        val place = marker?.tag as? RoomData ?: return null

        // 2. Inflate view and set title, address, and rating
        var view = LayoutInflater.from(context).inflate(R.layout.layout_roominfo_short, null)
      //  var title= view.findViewById<TextView>(R.id.txt_title)
      //  view.findViewById<Button>(R.id.btn_price).text = place.title
        return view
    }

    override fun getInfoWindow(marker: Marker): View? {
        // Return null to indicate that the
        // default window (white bubble) should be used
        return null
    }

}