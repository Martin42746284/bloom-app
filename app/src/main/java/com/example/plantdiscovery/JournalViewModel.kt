package com.example.plantdiscovery

import androidx.lifecycle.ViewModel
import com.example.plantdiscovery.model.Discovery
import com.example.plantdiscovery.repository.DiscoveryRepository
import kotlinx.coroutines.flow.StateFlow

class JournalViewModel(private val repository: DiscoveryRepository) : ViewModel() {
    val discoveries: StateFlow<List<Discovery>> = repository.discoveries

    fun addFakeDiscovery(discovery: Discovery) = repository.addFakeDiscovery(discovery)
    fun deleteDiscovery(discovery: Discovery) = repository.deleteDiscovery(discovery)
}
