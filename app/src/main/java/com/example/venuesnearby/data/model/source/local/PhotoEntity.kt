package com.example.venuesnearby.data.model.source.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.venuesnearby.data.model.domain.Photo
import com.example.venuesnearby.data.model.source.remote.PhotoNetworkModel

@Entity(tableName = "photo")
internal data class PhotoEntity(
    @PrimaryKey val id: String,
    val createdAt: Number,
    val prefix: String,
    val suffix: String
) {

    fun toPhoto(): Photo {
        return Photo(prefix, suffix)
    }

    companion object {
        fun from(photoNetworkModel: PhotoNetworkModel): PhotoEntity {
            return PhotoEntity(
                photoNetworkModel.id,
                photoNetworkModel.createdAt,
                photoNetworkModel.prefix,
                photoNetworkModel.suffix
            )
        }
    }
}
