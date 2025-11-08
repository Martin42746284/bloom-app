package com.example.plantdiscovery.config

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.plantdiscovery.dao.DiscoveryDao
import com.example.plantdiscovery.entities.DiscoveryEntity

@Database(entities = [DiscoveryEntity::class], version = 1)
abstract class PlantDatabase : RoomDatabase() {
    abstract fun discoveryDao(): DiscoveryDao
}
