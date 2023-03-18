package com.example.mapwork.models.response

import java.nio.DoubleBuffer

data class RoomData(
    val price: String,
    val id: String,
    val title: String,
    val location: String,
    val image: String,
    val latitude: Double,
    val longitude: Double
)
