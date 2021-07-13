package com.example.venuesnearby

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.venuesnearby.data.database.AppDatabase
import com.example.venuesnearby.data.database.dao.LocationDao
import com.example.venuesnearby.data.model.source.local.LocationEntity
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class LocationDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var locationDao: LocationDao

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        locationDao = db.locationDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertLocation() {
        val location = LocationEntity(
            id = 99,
            venueId = "id",
            address = "addressX",
            lat = 22.2,
            lng = 33.3,
            distance = 80,
            city = "city",
            state = "state",
            country = "country"
        )
        locationDao.insert(location)

        val mostRecentLocation = locationDao.getMostRecentLocation()
        Assert.assertEquals(mostRecentLocation, location)
    }
}