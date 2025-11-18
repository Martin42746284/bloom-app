package com.example.plantdiscovery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.plantdiscovery.config.AppDatabase
import com.example.plantdiscovery.navigation.NavGraph
import com.example.plantdiscovery.repository.AuthRepository
import com.example.plantdiscovery.repository.DiscoveryRepository
import com.example.plantdiscovery.ui.theme.PlantdiscoveryTheme
import com.example.plantdiscovery.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Activer Edge-to-Edge pour un UI moderne
        enableEdgeToEdge()

        // Initialiser Room Database
        val database = AppDatabase.getDatabase(applicationContext)
        val discoveryDao = database.discoveryDao()

        // Initialiser DiscoveryRepository avec DAO et Context
        val discoveryRepository = DiscoveryRepository(
            discoveryDao = discoveryDao,
            context = applicationContext
        )

        // Initialiser AuthRepository et AuthViewModel
        val authRepository = AuthRepository()
        val authViewModel = AuthViewModel(authRepository)

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
