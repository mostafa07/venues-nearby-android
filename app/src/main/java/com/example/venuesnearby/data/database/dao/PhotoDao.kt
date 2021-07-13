package com.example.venuesnearby.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.venuesnearby.data.model.source.local.PhotoEntity

@Dao
interface PhotoDao {

    @Insert
    fun insert(photo: PhotoEntity)

    @Query("SELECT * FROM PHOTO ORDER BY id DESC LIMIT 1")
    fun getMostRecentPhoto(): PhotoEntity
}