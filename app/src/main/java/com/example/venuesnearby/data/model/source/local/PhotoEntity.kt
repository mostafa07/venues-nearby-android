package com.example.venuesnearby.data.model.source.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.venuesnearby.data.model.domain.Photo
import com.example.venuesnearby.data.model.source.remote.PhotoNetworkModel

@Entity(tableName = "PHOTO")
data class PhotoEntity(
    @PrimaryKey val id: String,
    val venueId: String = "-1",
    @ColumnInfo(name = "created_at")
    val createdAt: Int,
    val prefix: String,
    val suffix: String
) {

    fun toPhoto(): Photo {
        return Photo(prefix, suffix)
    }

    companion object {
        fun from(photoNetworkModel: PhotoNetworkModel): PhotoEntity {
            return PhotoEntity(
                id = photoNetworkModel.id,
                createdAt = photoNetworkModel.createdAt,
                prefix = photoNetworkModel.prefix,
                suffix = photoNetworkModel.suffix
            )
        }
    }
}
