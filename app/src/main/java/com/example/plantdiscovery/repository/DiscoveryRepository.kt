package com.example.plantdiscovery.repository

import com.example.plantdiscovery.dao.DiscoveryDao
import com.example.plantdiscovery.model.Discovery
import kotlinx.coroutines.flow.Flow

class DiscoveryRepository(private val discoveryDao: DiscoveryDao) {

    // ✅ La fonction qui manquait !
    fun getAllDiscoveries(): Flow<List<Discovery>> {
        return discoveryDao.getAllDiscoveries()
    }

    suspend fun getDiscoveryById(id: Int): Discovery? {
        return discoveryDao.getDiscoveryById(id)
    }

    suspend fun insertDiscovery(discovery: Discovery) {
        discoveryDao.insertDiscovery(discovery)
    }

    suspend fun updateDiscovery(discovery: Discovery) {
        discoveryDao.updateDiscovery(discovery)
    }

    suspend fun deleteDiscovery(discovery: Discovery) {
        discoveryDao.deleteDiscovery(discovery)
    }
}
