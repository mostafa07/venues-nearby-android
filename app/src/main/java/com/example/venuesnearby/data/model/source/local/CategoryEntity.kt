package com.example.venuesnearby.data.model.source.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.venuesnearby.data.model.domain.Category
import com.example.venuesnearby.data.model.source.remote.CategoryNetworkModel

@Entity(tableName = "category")
internal data class CategoryEntity(
    @PrimaryKey val id: String,
    val name: String,
) {

    fun toCategory(): Category {
        return Category(name, null, null, null, null)
    }

    companion object {
        fun from(categoryNetworkModel: CategoryNetworkModel): CategoryEntity {
            return CategoryEntity(
                categoryNetworkModel.id,
                categoryNetworkModel.name
            )
        }
    }
}