package com.example.plantdiscovery.dao

import androidx.room.*
import com.example.plantdiscovery.model.Discovery
import kotlinx.coroutines.flow.Flow

@Dao
interface DiscoveryDao {

    @Query("SELECT * FROM discoveries ORDER BY timestamp DESC")
    fun getAllDiscoveries(): Flow<List<Discovery>>

    @Query("SELECT * FROM discoveries WHERE id = :id")
    suspend fun getDiscoveryById(id: Int): Discovery?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDiscovery(discovery: Discovery)

    @Update
    suspend fun updateDiscovery(discovery: Discovery)

    @Delete
    suspend fun deleteDiscovery(discovery: Discovery)
}

