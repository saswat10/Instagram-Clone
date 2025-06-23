package com.saswat10.instagramclone.screens.userScreens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Message
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.QuestionAnswer
import androidx.compose.material.icons.rounded.AccountBox
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Face
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.QuestionAnswer
import androidx.compose.material.icons.twotone.Home
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradient
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.saswat10.instagramclone.navigation.AllPostsScreen
import com.saswat10.instagramclone.navigation.ChatScreen
import com.saswat10.instagramclone.navigation.Notifications
import com.saswat10.instagramclone.navigation.ProfileScreen


enum class Destinations(
    val route: String,
    val label: String,
    val selectIcon: ImageVector,
    val unselectIcon: ImageVector,
    val contentDescription: String
) {
    HOME(AllPostsScreen::class.java.name, "Home", Icons.Rounded.Home, Icons.Outlined.Home, "Home"),
    CHAT(ChatScreen::class.java.name, "Chat", Icons.Rounded.QuestionAnswer, Icons.Outlined.QuestionAnswer, "Chat"),
    NOTIFICATIONS(
        Notifications::class.java.name,
        "Notifications",
        Icons.Rounded.Notifications,
        Icons.Outlined.Notifications,
        "Notifications"
    ),
    PROFILE(ProfileScreen::class.java.name, "Profile", Icons.Rounded.Face, Icons.Outlined.Face, "Profile"),
}

@Composable
fun MainScreen(
    updateProfile: (() -> Unit)


) {

    val navController = rememberNavController()
    val startDestination = Destinations.HOME.route
    var selectedDestination by rememberSaveable { mutableStateOf(startDestination) }
    var isVisible by rememberSaveable { mutableStateOf(true) }


    Scaffold(
        contentWindowInsets = WindowInsets(top = 0.dp),
//        containerColor = Color.Transparent,
        floatingActionButton = {
            AnimatedVisibility(
                visible = isVisible,
                enter = scaleIn(animationSpec = tween(500)),
                exit = scaleOut(animationSpec = tween(500))
            )
            {
                FloatingActionButton(
                    containerColor = MaterialTheme.colorScheme.primary,
                    onClick = {},
                    content = { Icon(imageVector = Icons.Rounded.Add, contentDescription = null) })
            }

        },
        bottomBar = {
            NavigationBar(
                windowInsets = NavigationBarDefaults.windowInsets,
                tonalElevation = 2.dp,
                containerColor = Color.Transparent,
                modifier = Modifier.background(MaterialTheme.colorScheme.surface)
            ) {
                Destinations.entries.forEachIndexed { index, destination ->
                    NavigationBarItem(
                        selected = (selectedDestination == destination.route),
                        onClick = {
                            isVisible = (Destinations.HOME.route == (destination.route))
                            navController.navigate(destination.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                            selectedDestination = destination.route
                        },
                        icon = {
                            Icon(
                                imageVector = if (selectedDestination == destination.route) destination.selectIcon else destination.unselectIcon,
                                contentDescription = destination.contentDescription,
                                modifier = Modifier.size(26.dp)
                            )
                        },
//                        label = {
//                            Text(destination.label)
//                        },
                        colors = NavigationBarItemColors(
                            selectedIconColor = MaterialTheme.colorScheme.surfaceTint,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.inversePrimary,
                            selectedIndicatorColor = MaterialTheme.colorScheme.surface,
                            unselectedTextColor = MaterialTheme.colorScheme.surface,
                            disabledIconColor = MaterialTheme.colorScheme.surfaceDim,
                            disabledTextColor = MaterialTheme.colorScheme.surfaceDim,
                        )
                    )
                }
            }
        }
    ) { _ ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
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
            composable<Notifications> { NotificationsScreen() }
        }
    }
}