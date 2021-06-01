package com.example.venuesnearby.data.model

import com.example.venuesnearby.data.model.source.remote.Checkin
import com.example.venuesnearby.data.model.source.remote.PhotoSource
import com.google.gson.annotations.SerializedName

data class Photo(
    val id: String,
    val createdAt: Number,
    @SerializedName("source")
    val photoSource: PhotoSource,
    val prefix: String,
    val suffix: String,
    val width: Number,
    val height: Number,
    val checkin: Checkin,
    val visibility: String,
) {
    val url: String
        get() = prefix + "original" + suffix
}