package com.example.plantdiscovery.dao


import androidx.room.*
import com.example.plantdiscovery.entities.DiscoveryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DiscoveryDao {
    @Query("SELECT * FROM discoveries ORDER BY timestamp DESC")
    fun getAll(): Flow<List<DiscoveryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(discovery: DiscoveryEntity)

    @Delete
    suspend fun delete(discovery: DiscoveryEntity)
}
