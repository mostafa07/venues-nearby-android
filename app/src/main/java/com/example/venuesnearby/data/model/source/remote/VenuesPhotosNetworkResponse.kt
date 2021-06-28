package com.example.venuesnearby.data.model.source.remote

data class VenuesPhotosApiResponse(val meta: Meta, val response: PhotosResponse)

data class PhotosResponse(val photos: Photos)

data class Photos(val count: Number, val items: List<PhotoNetworkModel>, val dupesRemoved: Number)

data class PhotoSource(val name: String, val url: String)

data class Checkin(
    val id: String,
    val createdAt: Number,
    val type: String,
    val timeZoneOffset: Number
)