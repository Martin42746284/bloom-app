package com.example.plantdiscovery.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.plantdiscovery.JournalViewModel
import com.example.plantdiscovery.repository.DiscoveryRepository
import com.example.plantdiscovery.ui.screens.*

sealed class Screen(val route: String) {
    object Auth : Screen("auth")
    object JournalList : Screen("journal_list")
    object Capture : Screen("capture")
    object Detail : Screen("detail/{discoveryId}") {
        fun createRoute(discoveryId: Int) = "detail/$discoveryId"
    }
}

@Composable
fun NavGraph(
    navController: NavHostController,
    repository: DiscoveryRepository      // injecte le repository ici !
) {
    val viewModel = remember { JournalViewModel(repository) }
    val discoveries by viewModel.discoveries.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Screen.Auth.route
    ) {
        composable(Screen.Auth.route) {
            AuthScreen(
                onSignInClick = { _, _ -> navController.navigate(Screen.JournalList.route) },
                onSignUpClick = { _, _ -> navController.navigate(Screen.JournalList.route) },
                onGoogleClick = { /* TODO... */ },
                isSignIn = true,
                onSwitchMode = {}
            )
        }

        composable(Screen.JournalList.route) {
            JournalListScreen(
                discoveries = discoveries,
                onAddClick = { navController.navigate(Screen.Capture.route) },
                onCardClick = { discovery ->
                    navController.navigate(Screen.Detail.createRoute(discovery.id))
                },
                onDeleteClick = { viewModel.deleteDiscovery(it) }
            )
        }

        composable(Screen.Capture.route) {
            CaptureScreen(
                imagePath = null,
                onCaptureClick = { /* TODO : ajouter logique de photo */ },
                onGalleryClick = { /* TODO : ajouter logique de galerie */ },
                loading = false,
                onCancel = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("discoveryId") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("discoveryId")
            val discovery = discoveries.find { it.id == id } ?: return@composable
            DetailScreen(
                discovery = discovery,
                onBack = { navController.popBackStack() },
                onDelete = {
                    viewModel.deleteDiscovery(discovery)
                    navController.popBackStack()
                }
            )
        }
    }
}
