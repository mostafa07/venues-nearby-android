package com.example.venuesnearby.data.model.source.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.venuesnearby.data.model.domain.Location
import com.example.venuesnearby.data.model.source.remote.LocationNetworkModel

@Entity(tableName = "LOCATION")
data class LocationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = -1L,
    val venueId: String = "-1",
    val address: String,
    val lat: Double,
    val lng: Double,
    val distance: Int,
    val city: String,
    val state: String,
    val country: String
) {

    fun toLocation(): Location {
        return Location(address, lat, lng, distance, city, state, country)
    }

    companion object {
        fun from(locationNetworkModel: LocationNetworkModel): LocationEntity {
            return LocationEntity(
                address = locationNetworkModel.address,
                lat = locationNetworkModel.lat,
                lng = locationNetworkModel.lng,
                distance = locationNetworkModel.distance,
                city = locationNetworkModel.city,
                state = locationNetworkModel.state,
                country = locationNetworkModel.country
            )
        }
    }
}
