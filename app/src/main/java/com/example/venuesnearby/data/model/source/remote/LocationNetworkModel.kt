package com.example.venuesnearby.data.model.source.remote

import com.example.venuesnearby.data.model.domain.Location

data class LocationNetworkModel(
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
) {

    fun toLocation(): Location {
        return Location(address, lat, lng, distance, city, state, country)
    }
}