package com.saswat10.instagramclone

import android.graphics.Color.TRANSPARENT
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.saswat10.instagramclone.navigation.RootNavGraph
import com.saswat10.instagramclone.ui.theme.InstagramCloneTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Install the splash screen
        val splashScreen = installSplashScreen()

        // Keep the splash screen on-screen until your content is ready.
        // For demonstration, we'll simulate some loading time.
        // In a real app, you'd replace this with your actual initialization logic.
        var keepOnScreen = true
        splashScreen.setKeepOnScreenCondition { keepOnScreen }

        // Simulate some loading
        runBlocking {
            delay(2000) // Simulate a 2-second loading time
            keepOnScreen = false
        }
        enableEdgeToEdge()
        setContent {
            // navigation
            val navController = rememberNavController()

            WindowCompat.setDecorFitsSystemWindows(window, false)

            // snackbar definition
            val snackbarHostState = remember { SnackbarHostState() }
            LaunchedEffect(Unit) {
                SnackBarManager.messages.collect { message ->
                    snackbarHostState.showSnackbar(message)
                }
            }

            InstagramCloneTheme {
                Scaffold(
                    snackbarHost = {
                        SnackbarHost(
                            snackbarHostState, modifier = Modifier.padding(
                                WindowInsets.ime.asPaddingValues()
                            )
                        )
                    }) { innerPadding ->
                    RootNavGraph(
                        firebaseAuth,
                        modifier = Modifier.padding(top = innerPadding.calculateTopPadding())
                    )
                }
            }
        }
    }

}


object SnackBarManager {
    private val _messages = MutableSharedFlow<String>(
        replay = 0,
        extraBufferCapacity = 1
    )
    val messages = _messages.asSharedFlow()

    suspend fun showMessage(message: String) {
        _messages.emit(message)
    }
}
