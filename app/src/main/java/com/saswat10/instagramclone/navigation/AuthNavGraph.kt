package com.saswat10.instagramclone.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.saswat10.instagramclone.screens.authScreens.LoginScreen
import com.saswat10.instagramclone.screens.authScreens.RegisterScreen
import com.saswat10.instagramclone.screens.authScreens.ResetPassword

fun NavGraphBuilder.authNavGraph(navController: NavHostController) {
    composable<AuthNavRoutes.LoginScreen> { backStack ->
        LoginScreen(navigateTo = { navController.navigate(route = it) })
    }
    composable<AuthNavRoutes.RegisterScreen> {
        RegisterScreen(navigateTo = { navController.navigate(route = it) })
    }
    composable<AuthNavRoutes.PasswordScreen> { ResetPassword(navigateBack = { navController.popBackStack() }) }
}