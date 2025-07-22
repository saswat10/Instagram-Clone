package com.saswat10.instagramclone.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.saswat10.instagramclone.screens.chatScreens.ChatScreen
import com.saswat10.instagramclone.screens.postScreens.AllPostsScreen
import com.saswat10.instagramclone.screens.postScreens.WritePostScreen
import com.saswat10.instagramclone.screens.searchScreens.SearchScreen
import com.saswat10.instagramclone.screens.userScreens.NotificationsScreen
import com.saswat10.instagramclone.screens.userScreens.ProfileScreen
import com.saswat10.instagramclone.screens.userScreens.UpdateProfileScreen


fun NavGraphBuilder.mainNavGraph(navController: NavController){
    composable<MainNavRoutes.UserFeedScreen>{ AllPostsScreen(navigateTo = {navController.navigate(it)}) }
    composable<MainNavRoutes.SearchScreen>{ SearchScreen() }
    composable<MainNavRoutes.ChatListScreen>{ ChatScreen() }
    composable<MainNavRoutes.NotificationScreen>{ NotificationsScreen() }
    composable<MainNavRoutes.ProfileScreen>{ ProfileScreen(navigateTo = { navController.navigate(it)}) }
    composable<MainNavRoutes.CreatePostScreen>{ WritePostScreen() }
    composable<MainNavRoutes.UpdateScreen> { UpdateProfileScreen(navigateTo = {navController.navigate(it)}) }
}