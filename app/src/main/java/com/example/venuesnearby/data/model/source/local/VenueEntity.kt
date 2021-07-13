package com.example.venuesnearby.data.model.source.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.venuesnearby.data.model.source.remote.VenueNetworkModel

@Entity(tableName = "VENUE")
data class VenueEntity(
    @PrimaryKey val id: String,
    val name: String
) {

    companion object {
        fun from(venueNetworkModel: VenueNetworkModel): VenueEntity {
            return VenueEntity(
                id = venueNetworkModel.id,
                name = venueNetworkModel.name
            )
        }
    }
}