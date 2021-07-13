package com.example.venuesnearby

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.venuesnearby.data.database.AppDatabase
import com.example.venuesnearby.data.database.dao.PhotoDao
import com.example.venuesnearby.data.model.source.local.PhotoEntity
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class PhotoDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var photoDao: PhotoDao

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        photoDao = db.photoDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertPhoto() {
        val photo = PhotoEntity(
            id = "id",
            venueId = "id",
            createdAt = 1004331,
            prefix = "prefix",
            suffix = "suffix"
        )
        photoDao.insert(photo)

        val mostRecentPhoto = photoDao.getMostRecentPhoto()
        Assert.assertEquals(mostRecentPhoto, photo)
    }
}