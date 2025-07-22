package com.saswat10.instagramclone.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.QuestionAnswer
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.rounded.Face
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.QuestionAnswer
import androidx.compose.material.icons.rounded.Search
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable

sealed class MainNavRoutes() {

    @Serializable data object UserFeedScreen: MainNavRoutes()
    @Serializable data object SearchScreen: MainNavRoutes()
    @Serializable data object CreatePostScreen: MainNavRoutes()
    @Serializable data object NotificationScreen: MainNavRoutes()
    @Serializable data object ProfileScreen: MainNavRoutes()
    @Serializable data object ChatListScreen: MainNavRoutes()
    @Serializable data object UpdateScreen: MainNavRoutes()
}

enum class BottomNavRoutes(
    val route: String,
    val label: String,
    val selectIcon: ImageVector,
    val unselectIcon: ImageVector,
    val contentDescription: String
) {
    HOME(MainNavRoutes.UserFeedScreen::class.java.canonicalName, "Home", Icons.Rounded.Home, Icons.Outlined.Home, "Home"),
    SEARCH(MainNavRoutes.SearchScreen::class.java.canonicalName, "Search", Icons.Rounded.Search, Icons.Outlined.Search, "Search"),
    CHAT(MainNavRoutes.ChatListScreen::class.java.canonicalName, "Chats", Icons.Rounded.QuestionAnswer, Icons.Outlined.QuestionAnswer, "Chat"),
    NOTIFICATIONS(
        MainNavRoutes.NotificationScreen::class.java.canonicalName,
        "Notifications",
        Icons.Rounded.Notifications,
        Icons.Outlined.Notifications,
        "Notifications"
    ),
    PROFILE(MainNavRoutes.ProfileScreen::class.java.canonicalName, "Profile", Icons.Rounded.Face, Icons.Outlined.Face, "Profile"),
}