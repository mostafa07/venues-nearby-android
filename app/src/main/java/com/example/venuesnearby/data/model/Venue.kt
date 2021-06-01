package com.example.venuesnearby.data.model

import com.example.venuesnearby.data.model.source.remote.Category
import com.example.venuesnearby.data.model.source.remote.Location
import com.example.venuesnearby.data.model.source.remote.VenuePage
import com.google.gson.annotations.Expose

data class Venue(
    val id: String,
    val name: String,
    val location: Location,
    val categories: List<Category>,
    val venuePage: VenuePage,

    // TODO test in-app serialization for this field whether it will be null or not
    @Expose(serialize = false, deserialize = false)
    var photos: List<Photo>?
)
