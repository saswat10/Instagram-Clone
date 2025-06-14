package com.saswat10.instagramclone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.google.firebase.auth.FirebaseAuth
import com.saswat10.instagramclone.screens.authScreens.LoginScreen
import com.saswat10.instagramclone.screens.authScreens.RegisterScreen
import com.saswat10.instagramclone.screens.authScreens.ResetPassword
import com.saswat10.instagramclone.screens.authScreens.UpdateProfile
import com.saswat10.instagramclone.ui.theme.InstagramCloneTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InstagramCloneTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    UpdateProfile(modifier = Modifier.padding(it))
                }
            }
        }
    }

}
