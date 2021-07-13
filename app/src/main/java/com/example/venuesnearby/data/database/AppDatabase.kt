package com.example.venuesnearby.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.venuesnearby.data.database.dao.CategoryDao
import com.example.venuesnearby.data.database.dao.LocationDao
import com.example.venuesnearby.data.database.dao.PhotoDao
import com.example.venuesnearby.data.database.dao.VenueDao
import com.example.venuesnearby.data.model.source.local.CategoryEntity
import com.example.venuesnearby.data.model.source.local.LocationEntity
import com.example.venuesnearby.data.model.source.local.PhotoEntity
import com.example.venuesnearby.data.model.source.local.VenueEntity

@Database(
    entities = [VenueEntity::class, PhotoEntity::class, LocationEntity::class, CategoryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract val venueDao: VenueDao
    abstract val photoDao: PhotoDao
    abstract val locationDao: LocationDao
    abstract val categoryDao: CategoryDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            synchronized(lock = this) {
                var instance = INSTANCE
                if (null == instance) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "app_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}