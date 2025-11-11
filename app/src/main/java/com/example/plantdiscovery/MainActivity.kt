package com.example.plantdiscovery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import com.example.plantdiscovery.repository.AuthRepository
import com.example.plantdiscovery.repository.DiscoveryRepository
import com.example.plantdiscovery.ui.theme.PlantDiscoveryTheme
import com.example.plantdiscovery.navigation.NavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlantDiscoveryTheme {
                val navController = rememberNavController()
                val discoveryRepository = remember { DiscoveryRepository() }
                val authViewModel = remember { AuthViewModel(AuthRepository()) }

                NavGraph(
                    navController = navController,
                    repository = discoveryRepository,
                    authViewModel = authViewModel
                )
            }
        }
    }
}
