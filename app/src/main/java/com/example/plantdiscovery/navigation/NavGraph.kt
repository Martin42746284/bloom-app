package com.example.plantdiscovery.navigation

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.*
import androidx.navigation.compose.*
import com.example.plantdiscovery.AuthViewModel
import com.example.plantdiscovery.JournalViewModel
import com.example.plantdiscovery.repository.DiscoveryRepository
import com.example.plantdiscovery.ui.screens.*
import com.google.firebase.auth.FirebaseAuth

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
    // ✅ Déclaré UNE SEULE FOIS au niveau du NavGraph
    val context = LocalContext.current
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
                onGoogleClick = {
                    // ✅ CORRIGÉ - Supprimé webClientId
                    authViewModel.signInWithGoogle(context = context) {
                        navController.navigate(Screen.JournalList.route) {
                            popUpTo(Screen.SignIn.route) { inclusive = true }
                        }
                    }
                },
                onGoToSignUp = {
                    navController.navigate(Screen.SignUp.route)
                },
                onErrorDismiss = { authViewModel.clearError() }
            )
        }

        // Sign Up Screen
        composable(Screen.SignUp.route) {
            val loading by authViewModel.loading.collectAsState()
            val error by authViewModel.error.collectAsState()

            SignUpScreen(
                loading = loading,
                error = error,
                onSignUpClick = { email, password ->
                    authViewModel.signUp(email, password) {
                        navController.navigate(Screen.JournalList.route) {
                            popUpTo(Screen.SignUp.route) { inclusive = true }
                        }
                    }
                },
                onGoogleClick = {
                    // ✅ CORRIGÉ - Supprimé webClientId
                    authViewModel.signInWithGoogle(context = context) {
                        navController.navigate(Screen.JournalList.route) {
                            popUpTo(Screen.SignUp.route) { inclusive = true }
                        }
                    }
                },
                onGoToSignIn = {
                    navController.popBackStack()
                },
                onErrorDismiss = { authViewModel.clearError() }
            )
        }


        // Journal List Screen
        composable(Screen.JournalList.route) {
            JournalListScreen(
                discoveries = discoveries,
                onAddClick = {
                    navController.navigate(Screen.Capture.route)
                },
                onCardClick = { discovery ->
                    navController.navigate(Screen.Detail.createRoute(discovery.id))
                },
                onDeleteClick = { discovery ->
                    viewModel.deleteDiscovery(discovery)
                }
            )
        }

//        // Capture Screen
//        composable(Screen.Capture.route) {
//            val context = LocalContext.current
//
//            CaptureScreen(
//                imagePath = null,
//                onCaptureClick = { /* Déjà géré dans CaptureScreen */ },
//                onGalleryClick = { /* Déjà géré dans CaptureScreen */ },
//                loading = false,
//                onCancel = {
//                    navController.popBackStack()
//                },
//                onSave = { plantName, imageUri ->
//                    viewModel.addDiscovery(plantName, imageUri, context)
//                    navController.popBackStack()
//                }
//            )
//        }

        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("discoveryId") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("discoveryId")
            val discovery = discoveries.find { it.id == id }

            if (discovery != null) {
                DetailScreen(
                    discovery = discovery,
                    onBack = { navController.popBackStack() },
                    onDelete = {
                        viewModel.deleteDiscovery(discovery)  // ✅ Supprime l'image + DB
                        navController.popBackStack()
                    }
                )
            }
        }

    }
}
