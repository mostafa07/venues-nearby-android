package com.example.venuesnearby.data.model.source.remote

import com.example.venuesnearby.data.model.domain.Category

data class CategoryNetworkModel(
    val id: String,
    val name: String,
    val pluralName: String,
    val shortName: String,
    val icon: Icon,
    val primary: Boolean
) {

    fun toCategory(): Category {
        return Category(name, pluralName, shortName, icon, primary)
    }
}