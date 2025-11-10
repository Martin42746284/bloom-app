package com.example.plantdiscovery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.example.plantdiscovery.model.Discovery
import com.example.plantdiscovery.repository.DiscoveryRepository
import com.example.plantdiscovery.ui.screens.JournalListScreen
import com.example.plantdiscovery.ui.theme.PlantDiscoveryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlantDiscoveryTheme {
                val viewModel = remember { JournalViewModel(DiscoveryRepository()) }
                val discoveries by viewModel.discoveries.collectAsState()

                JournalListScreen(
                    discoveries = discoveries,
                    onAddClick = { viewModel.addFakeDiscovery(
                        Discovery(
                            3,
                            "demo",
                            "Pothos",
                            "Le Pothos est quasi impossible à tuer.",
                            "/storage/demo/pothos.jpg",
                            System.currentTimeMillis()
                        )
                    ) },
                    onCardClick = {},
                    onDeleteClick = { viewModel.deleteDiscovery(it) }
                )
            }
        }
    }
}
