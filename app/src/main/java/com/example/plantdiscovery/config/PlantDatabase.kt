package com.example.plantdiscovery.config

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.plantdiscovery.dao.DiscoveryDao
import com.example.plantdiscovery.entities.DiscoveryEntity
import android.content.Context
import androidx.room.Room
import com.example.plantdiscovery.model.Discovery

@Database(
    entities = [Discovery::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun discoveryDao(): DiscoveryDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "plant_discovery_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

