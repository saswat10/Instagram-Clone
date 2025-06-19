package com.saswat10.instagramclone.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.saswat10.instagramclone.navigation.LoginScreen
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


@Composable
fun AuthNavGraph(navController: NavHostController){
    NavHost(navController = navController, startDestination = LoginScreen) {
        composable<LoginScreen> {backStack ->
            val login: LoginScreen = backStack.toRoute()
            LoginScreen(
                navigateToRegister = {
                    navController.navigate(route = RegisterScreen, )
                },
                navigateToPassword = {
                    navController.navigate(route = PasswordScreen)
                }
            )
        }
        composable<RegisterScreen> { RegisterScreen(navController = navController) }
        composable<PasswordScreen> { ResetPassword(navController = navController) }
    }
}