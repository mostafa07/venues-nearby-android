package com.example.venuesnearby.data.model.source.local

import androidx.room.Entity
import com.example.venuesnearby.data.model.domain.Location
import com.example.venuesnearby.data.model.source.remote.LocationNetworkModel

@Entity(tableName = "location")
internal data class LocationEntity(
    val address: String,
    val lat: Double,
    val lng: Double,
    val distance: Int,
    val city: String,
    val state: String,
    val country: String,
) {

    fun toLocation(): Location {
        return Location(address, lat, lng, distance, city, state, country)
    }

    companion object {
        fun from(locationNetworkModel: LocationNetworkModel): LocationEntity {
            return LocationEntity(
                locationNetworkModel.address,
                locationNetworkModel.lat,
                locationNetworkModel.lng,
                locationNetworkModel.distance,
                locationNetworkModel.city,
                locationNetworkModel.state,
                locationNetworkModel.country
            )
        }
    }
}
