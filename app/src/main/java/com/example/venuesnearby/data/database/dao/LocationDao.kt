package com.example.venuesnearby.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.venuesnearby.data.model.source.local.LocationEntity

@Dao
interface LocationDao {

    @Insert
    fun insert(location: LocationEntity)

    @Query("SELECT * FROM location ORDER BY id DESC LIMIT 1")
    fun getMostRecentLocation(): LocationEntity
}