package com.saswat10.instagramclone.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.saswat10.instagramclone.navigation.BottomNavRoutes
import com.saswat10.instagramclone.navigation.MainNavRoutes
import com.saswat10.instagramclone.navigation.authNavGraph
import com.saswat10.instagramclone.navigation.mainNavGraph
import com.saswat10.instagramclone.utils.SnackBarManager
import com.saswat10.instagramclone.viewmodels.MainViewModel
import timber.log.Timber

@Composable
fun AppNavigationScreen(
    viewModel: MainViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route


    val routesWithNavBar = listOf(
        MainNavRoutes.UserFeedScreen::class.java.canonicalName,
        MainNavRoutes.SearchScreen::class.java.canonicalName,
        MainNavRoutes.ProfileScreen::class.java.canonicalName,
        MainNavRoutes.NotificationScreen::class.java.canonicalName,
        MainNavRoutes.ChatListScreen::class.java.canonicalName
    )

    val showNavBar = routesWithNavBar.any { route ->
        Timber.d("$currentRoute, $route")
        currentRoute?.equals(route) == true
    }

    val snackBarHostState = remember { SnackbarHostState() }
    LaunchedEffect(Unit) {
        SnackBarManager.messages.collect { message ->
            snackBarHostState.showSnackbar(message)
        }
    }




    Scaffold(
        bottomBar = {
            if (!uiState.loading && showNavBar) BottomNavBar(
                navController, currentRoute
            )
        }) { padValues ->
        Column(modifier = Modifier.padding(padValues)) {
            if (!uiState.loading) {
                NavHost(
                    navController = navController, startDestination = uiState.startDestination
                ) {
                    authNavGraph(navController)
                    mainNavGraph(navController)
                }
            } else {
                CircularProgressIndicator()
            }
        }

    }

}


@Composable
fun BottomNavBar(navController: NavHostController, currentRoute: String?) {
    NavigationBar() {
        BottomNavRoutes.entries.forEachIndexed { index, routes ->
            NavigationBarItem(
                selected = (currentRoute?.startsWith(routes.route) == true),
                onClick = {
                    navController.navigate(routes.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(routes.selectIcon, routes.label) },
                label = { Text(routes.label) })
        }
    }
}