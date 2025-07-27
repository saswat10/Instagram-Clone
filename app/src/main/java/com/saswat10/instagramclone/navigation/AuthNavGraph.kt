package com.saswat10.instagramclone.navigation

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.saswat10.instagramclone.presentation.screens.authScreens.LoginScreen
import com.saswat10.instagramclone.presentation.screens.authScreens.RegisterScreen
import com.saswat10.instagramclone.presentation.screens.authScreens.ResetPassword

fun NavGraphBuilder.authNavGraph(navController: NavHostController) {
    composable<AuthNavRoutes.LoginScreen> { backStack ->
        LoginScreen(navigateTo = { navController.navigate(route = it) }, navigateOnLogin = {
            navController.navigate(
                MainNavRoutes.UserFeedScreen
            ) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                    inclusive = true
                }
            }
        })
    }
    composable<AuthNavRoutes.RegisterScreen> {
        RegisterScreen(navigateTo = { navController.navigate(route = it) }, navigateToUpdate = {
            navController.navigate(
                MainNavRoutes.UpdateScreen
            ) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                    inclusive = true
                }
            }
        })
    }
    composable<AuthNavRoutes.PasswordScreen> { ResetPassword(navigateBack = { navController.popBackStack() }) }
}