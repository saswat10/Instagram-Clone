package com.saswat10.instagramclone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.firebase.auth.FirebaseAuth
import com.saswat10.instagramclone.presentation.screens.AppNavigationScreen
import com.saswat10.instagramclone.presentation.screens.postScreens.CreatePostScreen
import com.saswat10.instagramclone.ui.theme.InstagramCloneTheme
import com.saswat10.instagramclone.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()
    @Inject
    lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                lightScrim = android.graphics.Color.TRANSPARENT,
                darkScrim = android.graphics.Color.TRANSPARENT,
            ),
            navigationBarStyle = SystemBarStyle.auto(
                lightScrim = lightScrim,
                darkScrim = darkScrim,
            ) ,
        )
        splashScreen.setKeepOnScreenCondition { mainViewModel.uiState.value.loading }
        setContent {
            InstagramCloneTheme {
                AppNavigationScreen(mainViewModel)
            }
        }
    }


    private val lightScrim = android.graphics.Color.argb(0xe6, 0xFF, 0xFF, 0xFF)
    private val darkScrim = android.graphics.Color.argb(0x80, 0x1b, 0x1b, 0x1b)
}
