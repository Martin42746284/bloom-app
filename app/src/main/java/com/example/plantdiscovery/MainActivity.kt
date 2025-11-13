package com.example.plantdiscovery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.plantdiscovery.config.AppDatabase
import com.example.plantdiscovery.navigation.NavGraph
import com.example.plantdiscovery.repository.AuthRepository
import com.example.plantdiscovery.repository.DiscoveryRepository
import com.example.plantdiscovery.ui.theme.PlantdiscoveryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialiser la base de données
        val database = AppDatabase.getDatabase(applicationContext)
        val discoveryDao = database.discoveryDao()
        val discoveryRepository = DiscoveryRepository(discoveryDao)

        // Initialiser les ViewModels
        val authViewModel = AuthViewModel(AuthRepository())

        setContent {
            PlantdiscoveryTheme {
                val navController = rememberNavController()
                NavGraph(
                    navController = navController,
                    repository = discoveryRepository,
                    authViewModel = authViewModel
                )
            }
        }
    }
}

