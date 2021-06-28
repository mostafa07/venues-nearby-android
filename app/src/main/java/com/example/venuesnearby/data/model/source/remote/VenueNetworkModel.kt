package com.example.venuesnearby.data.model.source.remote

import com.example.venuesnearby.data.model.domain.Venue
import com.google.gson.annotations.Expose

data class VenueNetworkModel(
    val id: String,
    val name: String,
    val location: LocationNetworkModel,
    val categories: List<CategoryNetworkModel>,
    val venuePage: VenuePage?,

    @Expose(serialize = false, deserialize = false)
    var photos: List<PhotoNetworkModel>?
) {

    fun toVenue(): Venue {
        return Venue(
            name,
            location.toLocation(),
            categories.map { it.toCategory() },
            photos?.get(0)?.toPhoto()
        )
    }
}
