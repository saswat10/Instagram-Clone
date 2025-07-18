package com.saswat10.instagramclone.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.saswat10.instagramclone.screens.authScreens.LoginScreen
import com.saswat10.instagramclone.screens.authScreens.RegisterScreen
import com.saswat10.instagramclone.screens.authScreens.ResetPassword
import kotlinx.serialization.Serializable


@Serializable
data object LoginScreen

@Serializable
data object RegisterScreen

@Serializable
data object PasswordScreen

fun NavGraphBuilder.authNavGraph(navController: NavHostController) {
    composable<LoginScreen> { backStack ->
        val login: LoginScreen = backStack.toRoute()
        LoginScreen(
            navigateToRegister = {
                navController.navigate(route = RegisterScreen)
            },
            navigateToPassword = {
                navController.navigate(route = PasswordScreen)
            }
        )
    }
    composable<RegisterScreen> {
        RegisterScreen(
            navigateToUpdate = {
                navController.navigate(route = UpdateProfile(navigateToDiscover = true))
            },
            onBack = {
                navController.popBackStack()
            }

        )
    }
    composable<PasswordScreen> { ResetPassword(navigateBack = { navController.popBackStack() }) }
}