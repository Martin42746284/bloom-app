package com.example.plantdiscovery.navigation

import androidx.compose.runtime.*
import androidx.navigation.*
import androidx.navigation.compose.*
import com.example.plantdiscovery.AuthViewModel
import com.example.plantdiscovery.JournalViewModel
import com.example.plantdiscovery.repository.DiscoveryRepository
import com.example.plantdiscovery.ui.screens.*

sealed class Screen(val route: String) {
    object SignIn : Screen("sign_in")
    object SignUp : Screen("sign_up")
    object JournalList : Screen("journal_list")
    object Capture : Screen("capture")
    object Detail : Screen("detail/{discoveryId}") {
        fun createRoute(discoveryId: Int) = "detail/$discoveryId"
    }
}

@Composable
fun NavGraph(
    navController: NavHostController,
    repository: DiscoveryRepository,
    authViewModel: AuthViewModel
) {
    val viewModel = remember { JournalViewModel(repository) }
    val discoveries by viewModel.discoveries.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Screen.SignIn.route
    ) {
        // Sign In Screen
        composable(Screen.SignIn.route) {
            val loading by authViewModel.loading.collectAsState()
            val error by authViewModel.error.collectAsState()
            SignInScreen(
                loading = loading,
                error = error,
                onSignInClick = { email, password ->
                    authViewModel.signIn(email, password) {
                        navController.navigate(Screen.JournalList.route) {
                            popUpTo(Screen.SignIn.route) { inclusive = true }
                        }
                    }
                },
                onGoogleClick = { /* TODO: Google Auth */ },
                onGoToSignUp = { navController.navigate(Screen.SignUp.route) },
                onErrorDismiss = { authViewModel.clearError() }
            )
        }

        // Sign Up Screen
        composable(Screen.SignUp.route) {
            val loading by authViewModel.loading.collectAsState()
            val error by authViewModel.error.collectAsState()
            val success by authViewModel.successSignUp.collectAsState()
            SignUpScreen(
                loading = loading,
                error = error,
                success = success,
                onSignUpClick = { email, password, confirm ->
                    authViewModel.signUp(email, password, confirm)
                },
                onGoToSignIn = {
                    authViewModel.clearSignUpSuccess()
                    navController.popBackStack(Screen.SignIn.route, false)
                },
                onErrorDismiss = { authViewModel.clearError() },
                onSuccessDismiss = {
                    authViewModel.clearSignUpSuccess()
                    navController.popBackStack(Screen.SignIn.route, false)
                }
            )
            // Redirect auto après succès !
            LaunchedEffect(success) {
                if (success) {
                    authViewModel.clearSignUpSuccess()
                    navController.popBackStack(Screen.SignIn.route, false)
                }
            }
        }

        // Journal List Screen
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

        // Capture Screen
        composable(Screen.Capture.route) {
            CaptureScreen(
                imagePath = null,
                onCaptureClick = { /* TODO : logique photo */ },
                onGalleryClick = { /* TODO : logique galerie */ },
                loading = false,
                onCancel = { navController.popBackStack() }
            )
        }

        // Detail Screen
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
