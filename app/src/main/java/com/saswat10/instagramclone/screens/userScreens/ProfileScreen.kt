package com.saswat10.instagramclone.screens.userScreens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.saswat10.instagramclone.components.user.ProfileCard

@Composable
fun ProfileScreen() {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp)) {
        LazyColumn() {
            
            item { ProfileCard() }
        }


    }
}