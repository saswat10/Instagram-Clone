package com.saswat10.instagramclone.screens.userScreens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.saswat10.instagramclone.components.posts.PostCard
import com.saswat10.instagramclone.navigation.UpdateProfile

@Composable
fun AllPostsScreen(updateProfile: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn {
            item {
                PostCard()
                PostCard()
                PostCard()
            }
        }

    }
}