package com.example.venuesnearby.data.model.source.local

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.venuesnearby.data.model.domain.Venue
import com.example.venuesnearby.data.model.source.remote.VenueNetworkModel

@Entity(tableName = "venue")
internal data class VenueEntity(
    @PrimaryKey val id: String,
    val name: String,
    @Embedded val location: LocationEntity,
    val categories: List<CategoryEntity>,
    var photo: PhotoEntity?
) {

    fun toVenue(): Venue {
        return Venue(
            name,
            location.toLocation(),
            categories.map { it.toCategory() },
            photo?.toPhoto()
        )
    }

    companion object {
        fun from(venueNetworkModel: VenueNetworkModel): VenueEntity {
            return VenueEntity(
                venueNetworkModel.id,
                venueNetworkModel.name,
                LocationEntity.from(venueNetworkModel.location),
                venueNetworkModel.categories.map { CategoryEntity.from(it) },
                venueNetworkModel.photos?.get(0)?.let { PhotoEntity.from(it) }
            )
        }
    }
}