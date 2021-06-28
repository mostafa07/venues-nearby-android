package com.example.venuesnearby.data.model.domain

import com.example.venuesnearby.data.model.source.remote.Icon

data class Category(
    val name: String,
    val pluralName: String?,
    val shortName: String?,
    val icon: Icon?,
    val primary: Boolean?
)