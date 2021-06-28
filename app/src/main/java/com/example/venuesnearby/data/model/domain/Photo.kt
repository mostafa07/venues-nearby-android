package com.example.venuesnearby.data.model.domain

data class Photo(
    val prefix: String,
    val suffix: String
) {
    val url: String
        get() = prefix + "original" + suffix
}