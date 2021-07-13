package com.example.venuesnearby.data.model.source.local

import androidx.room.Embedded
import androidx.room.Relation

data class VenueWithDetails(
    @Embedded val venue: VenueEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "venueId"
    )
    val location: LocationEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "venueId"
    )
    val categories: List<CategoryEntity>,
    @Relation(
        parentColumn = "id",
        entityColumn = "venueId"
    )
    val photo: PhotoEntity?
)