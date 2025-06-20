package com.saswat10.instagramclone.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun RootNavGraph(firebaseAuth: FirebaseAuth) {
    val navController = rememberNavController()
    val currentUser = remember { mutableStateOf(firebaseAuth.currentUser) }


    firebaseAuth.addAuthStateListener {
        currentUser.value = it.currentUser
    }


    val startDestination = if (currentUser.value == null) {
        LoginScreen
    } else {
        UpdateProfile
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = { fadeIn(animationSpec = tween(400)) + slideInHorizontally(initialOffsetX = { it }) },
        exitTransition = { fadeOut(animationSpec = tween(400)) + slideOutHorizontally(targetOffsetX = { -it }) },
        popEnterTransition = {
            fadeIn(animationSpec = tween(300)) + slideInHorizontally(
                initialOffsetX = { -it })
        },
        popExitTransition = {
            fadeOut(animationSpec = tween(300)) + slideOutHorizontally(
                targetOffsetX = { it })
        },
    ) {
        authNavGraph(navController)
        mainNavGraph(navController)
    }
}


