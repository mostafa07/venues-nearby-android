package com.example.venuesnearby.data.model.domain

data class Venue(
    val name: String,
    val location: Location,
    val categories: List<Category>,
    var photo: Photo?
)