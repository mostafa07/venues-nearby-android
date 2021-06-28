package com.example.venuesnearby.data.model.domain

data class Location(
    val address: String?,
    val lat: Double?,
    val lng: Double?,
    val distance: Int?,
    val city: String?,
    val state: String?,
    val country: String?,
)
