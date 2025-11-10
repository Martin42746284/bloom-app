package com.example.plantdiscovery.repository

import com.example.plantdiscovery.model.Discovery
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DiscoveryRepository {

    // Simuler la donnée (pour tests UI)
    private val _discoveries = MutableStateFlow(
        listOf(
            Discovery(1, "demo", "Ficus lyrata", "Le Ficus est aussi appelé figuier lyre.", "", System.currentTimeMillis()),
            Discovery(2, "demo", "Monstera Deliciosa", "Le Monstera a des trous décoratifs dans ses feuilles.", "", System.currentTimeMillis()-86400000)
        )
    )
    val discoveries: StateFlow<List<Discovery>> = _discoveries

    fun addFakeDiscovery(discovery: Discovery) {
        _discoveries.value += discovery
    }
    fun deleteDiscovery(discovery: Discovery) {
        _discoveries.value = _discoveries.value.filterNot { it.id == discovery.id }
    }
}