package com.example.venuesnearby

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.venuesnearby.data.database.AppDatabase
import com.example.venuesnearby.data.database.dao.VenueDao
import com.example.venuesnearby.data.model.source.local.*
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class VenueDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var venueDao: VenueDao

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        venueDao = db.venueDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertVenue() {
        val venue = VenueEntity(id = "id", name = "name")
        venueDao.insert(venue)

        val mostRecentVenue = venueDao.getMostRecentVenue()
        Assert.assertEquals(mostRecentVenue, venue)
    }

    @Test
    @Throws(Exception::class)
    fun insertVenueWithDetails() {
        val venueWithDetails = VenueWithDetails(
            venue = VenueEntity(id = "id", name = "name"),
            LocationEntity(
                id = 99,
                venueId = "id",
                address = "addressX",
                lat = 22.2,
                lng = 33.3,
                distance = 80,
                city = "city",
                state = "state",
                country = "country"
            ),
            listOf(
                CategoryEntity(id = "id1", venueId = "id", name = "name1"),
                CategoryEntity(id = "id2", venueId = "id", name = "name2")
            ),
            PhotoEntity(
                id = "id",
                venueId = "id",
                createdAt = 1004331,
                prefix = "prefix",
                suffix = "suffix"
            )
        )

        venueDao.insertVenueWithDetails(
            venueWithDetails.venue,
            venueWithDetails.location,
            venueWithDetails.categories,
            venueWithDetails.photo!!,
        )

        val mostRecentVenueWithDetails = venueDao.getMostRecentVenueWithDetails()
        Assert.assertEquals(venueWithDetails, mostRecentVenueWithDetails)
    }
}