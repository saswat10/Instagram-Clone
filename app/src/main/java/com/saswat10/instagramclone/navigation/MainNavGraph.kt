package com.saswat10.instagramclone.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.saswat10.instagramclone.screens.postScreens.WritePostScreen
import com.saswat10.instagramclone.screens.userScreens.MainScreen
import com.saswat10.instagramclone.screens.userScreens.UpdateProfileScreen
import kotlinx.serialization.Serializable


@Serializable
data class UpdateProfile(val navigateToDiscover: Boolean = false)

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

@Serializable
data object SearchScreen

@Serializable
data object WritePost

fun NavGraphBuilder.mainNavGraph(navController: NavHostController) {
    composable<UpdateProfile> {
        val args = it.toRoute<UpdateProfile>()
        UpdateProfileScreen(
            navigateToDiscover = args.navigateToDiscover,
            navigateToMainScreen = {
                if (args.navigateToDiscover) {
                    navController.navigate(MainScreen) {
                        popUpTo(args) {
                            inclusive = true
                        }
                    }
                }
            },
            onBack = {
                navController.popBackStack()
            }
        )
    }
    composable<MainScreen> {
        MainScreen(
            updateProfile = {
                navController.navigate(
                    UpdateProfile(
                        navigateToDiscover = false,
                    )
                )
            },
            navigateWritePost = {
                navController.navigate(
                    WritePost
                )
            }
        )
    }
    composable<WritePost> {
        WritePostScreen()
    }
}