package com.saswat10.instagramclone.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.createGraph
import androidx.navigation.navigation
import com.saswat10.instagramclone.screens.userScreens.AllPostsScreen
import com.saswat10.instagramclone.screens.userScreens.ChatScreen
import com.saswat10.instagramclone.screens.userScreens.MainScreen
import com.saswat10.instagramclone.screens.userScreens.ProfileScreen
import com.saswat10.instagramclone.screens.userScreens.UpdateProfile
import kotlinx.serialization.Serializable


@Serializable
data object UpdateProfile

@Serializable
data object MainScreen

@Serializable
data object AllPostsScreen

@Serializable
data object ChatScreen

@Serializable
data object ProfileScreen

@Serializable
data object Notifications


fun NavGraphBuilder.mainNavGraph(navController: NavHostController) {
    composable<UpdateProfile> {
        UpdateProfile(
            navigateToMainScreen = {
                navController.navigate(MainScreen) {
                    popUpTo(UpdateProfile) {
                        inclusive = true
                    }
                }
            }
        )
    }
    composable<MainScreen> { MainScreen(
        updateProfile = {
            navController.navigate(UpdateProfile)
        }
    ) }
}