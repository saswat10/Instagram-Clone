package com.saswat10.instagramclone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.saswat10.instagramclone.navigation.RootNavGraph
import com.saswat10.instagramclone.ui.theme.InstagramCloneTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                    modifier = Modifier.fillMaxSize(),
                    topBar = {},
                    snackbarHost = {
                        SnackbarHost(
                            snackbarHostState, modifier = Modifier.padding(
                                WindowInsets.ime.asPaddingValues()
                            )
                        )
                    }) { it ->
                    Column(Modifier.padding(it)) {
                        RootNavGraph(firebaseAuth)
                    }
                }
            }
        }
    }

}


object SnackBarManager {
    private val _messages = MutableSharedFlow<String>()
    val messages = _messages.asSharedFlow()

    suspend fun showMessage(message: String) {
        _messages.emit(message)
    }
}
