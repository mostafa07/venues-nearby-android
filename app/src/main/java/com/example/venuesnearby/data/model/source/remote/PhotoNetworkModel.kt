package com.example.venuesnearby.data.model.source.remote

import com.example.venuesnearby.data.model.domain.Photo
import com.google.gson.annotations.SerializedName

data class PhotoNetworkModel(
    val id: String,
    val createdAt: Int,
    @SerializedName("source")
    val photoSource: PhotoSource,
    val prefix: String,
    val suffix: String,
    val width: Number,
    val height: Number,
    val checkin: Checkin,
    val visibility: String
) {

    fun toPhoto(): Photo {
        return Photo(prefix, suffix)
    }
}