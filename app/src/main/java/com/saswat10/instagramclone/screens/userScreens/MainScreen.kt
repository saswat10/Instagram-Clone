package com.saswat10.instagramclone.screens.userScreens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Message
import androidx.compose.material.icons.rounded.AccountBox
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.saswat10.instagramclone.navigation.AllPostsScreen
import com.saswat10.instagramclone.navigation.ChatScreen
import com.saswat10.instagramclone.navigation.ProfileScreen


enum class Destinations(
    val route: Any,
    val label: String,
    val icon: ImageVector,
    val contentDescription: String
) {
    HOME(AllPostsScreen, "Home", Icons.Rounded.Home, "Home"),
    CHAT(ChatScreen, "Chat", Icons.AutoMirrored.Rounded.Message, "Chat"),
    PROFILE(ProfileScreen, "Profile", Icons.Rounded.AccountBox, "Profile")
}

@Composable
fun MainScreen(
    updateProfile: (() -> Unit)


) {

    val navController = rememberNavController()
    val startDestination = Destinations.HOME.route
    var selectedDestination by remember { mutableStateOf(startDestination) }


    Scaffold(
        bottomBar = {
            NavigationBar(windowInsets = NavigationBarDefaults.windowInsets) {
                Destinations.entries.forEachIndexed { index, destination ->
                    NavigationBarItem(
                        selected = (selectedDestination == destination.route),
                        onClick = {
                            navController.navigate(destination.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                            selectedDestination = destination.route
                        },
                        icon = {
                            Icon(
                                imageVector = destination.icon,
                                contentDescription = destination.contentDescription,
                                modifier = Modifier.size(26.dp)
                            )
                        },
                        label = {
                            Text(destination.label)
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding),
            enterTransition = {
                fadeIn(animationSpec = tween(400)) + slideInHorizontally(
                    initialOffsetX = { it })
            },
            exitTransition = {
                fadeOut(animationSpec = tween(400)) + slideOutHorizontally(
                    targetOffsetX = { -it })
            },
            popEnterTransition = {
                fadeIn(animationSpec = tween(400)) + slideInHorizontally(
                    initialOffsetX = { -it })
            },
            popExitTransition = {
                fadeOut(animationSpec = tween(400)) + slideOutHorizontally(
                    targetOffsetX = { it })
            },
        ) {
            composable<AllPostsScreen> { AllPostsScreen({ updateProfile() }) }
            composable<ChatScreen> { ChatScreen() }
            composable<ProfileScreen> { ProfileScreen() }
        }
    }
}