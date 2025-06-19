package com.saswat10.instagramclone.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.createGraph
import androidx.navigation.navigation
import com.saswat10.instagramclone.screens.userScreens.UpdateProfile
import kotlinx.serialization.Serializable


@Serializable
data object UpdateProfile

fun NavGraphBuilder.mainNavGraph(navController: NavHostController) {
        composable<UpdateProfile> { UpdateProfile() }
}