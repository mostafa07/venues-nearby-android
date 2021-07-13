package com.example.venuesnearby.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.venuesnearby.data.model.source.local.CategoryEntity

@Dao
interface CategoryDao {

    @Insert
    fun insert(category: CategoryEntity)

    @Insert
    fun insertAll(categories: List<CategoryEntity>)

    @Query("SELECT * FROM CATEGORY WHERE id = :id")
    fun getById(id: String): CategoryEntity

    @Query("SELECT * FROM CATEGORY ORDER BY id DESC LIMIT 1")
    fun getMostRecentCategory(): CategoryEntity
}