package com.example.mapwork.models.response

data class PropertyDataResponse(
    val city: String,
    val date_time: String,
    val facilities: List<Facility>,
    val id: Int,
    val images: List<Image>,
    val landloard_id: Int,
    val landmark: String,
    val latitude: Double,
    val longtitude: Double,
    val pincode: String,
    val plot_number: String,
    val price: String,
    val property_type: String,
    val state: String,
    val street: String
)
