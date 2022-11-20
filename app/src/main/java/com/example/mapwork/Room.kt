package com.example.mapwork

import com.google.android.gms.maps.model.LatLng

data class Room(var imageUrl: List<String>,
                var roomname: String,
                var address: String,
                var price: String,
                var description: String,
                var creatorname:String,
                var creatorprofileurl: String,
                var rating:String,
                var lag: LatLng
)
