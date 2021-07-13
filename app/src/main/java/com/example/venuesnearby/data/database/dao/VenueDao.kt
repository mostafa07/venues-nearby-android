package com.example.venuesnearby.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.venuesnearby.data.model.source.local.*

@Dao
interface VenueDao {

    @Insert
    fun insert(venue: VenueEntity)

    @Insert
    fun insertVenueWithDetails(
        venue: VenueEntity,
        location: LocationEntity,
        categories: List<CategoryEntity>,
        photo: PhotoEntity
    )

    @Query("SELECT * FROM VENUE WHERE id = :id")
    fun getVenueById(id: String): VenueEntity

    @Query("SELECT * FROM VENUE ORDER BY id DESC LIMIT 1")
    fun getMostRecentVenue(): VenueEntity

    @Transaction
    @Query("SELECT * FROM VENUE ORDER BY id DESC LIMIT 1")
    fun getMostRecentVenueWithDetails(): VenueWithDetails

    @Transaction
    @Query("SELECT * FROM VENUE")
    fun getVenuesWithDetails(): List<VenueWithDetails>
}