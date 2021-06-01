package com.example.venuesnearby.data.model.source.remote

data class Location(
    val address: String,
    val crossStreet: String,
    val lat: Double,
    val lng: Double,
    val labeledLatLngs: List<LabeledLatLngs>,
    val distance: Int,
    val postalCode: String,
    val cc: String,
    val city: String,
    val state: String,
    val country: String,
    val formattedAddress: List<String>
)