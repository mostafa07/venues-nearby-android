package com.example.venuesnearby.data.model.source.remote

data class Category(
    val id: String,
    val name: String,
    val pluralName: String,
    val shortName: String,
    val icon: Icon,
    val primary: Boolean
)