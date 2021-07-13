package com.example.venuesnearby

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.venuesnearby.data.database.AppDatabase
import com.example.venuesnearby.data.database.dao.CategoryDao
import com.example.venuesnearby.data.model.source.local.CategoryEntity
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class CategoryDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var categoryDao: CategoryDao

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        categoryDao = db.categoryDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertCategory() {
        val category = CategoryEntity("id", "venueId", "name")
        categoryDao.insert(category)

        val mostRecentCategory = categoryDao.getMostRecentCategory()
        Assert.assertEquals(mostRecentCategory, category)
    }
}