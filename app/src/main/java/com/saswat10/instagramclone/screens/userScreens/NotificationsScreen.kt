package com.saswat10.instagramclone.screens.userScreens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.saswat10.instagramclone.components.user.ProfilePreview
import com.saswat10.instagramclone.models.domain.User.UserPreview

@Composable
fun NotificationsScreen() {
    Column(modifier = Modifier.fillMaxWidth().padding(10.dp)) {
        ProfilePreview(UserPreview(profilePicUrl = null, username = "saswat10", userId = "123", fullName = "Saswat Samal"))
        ProfilePreview(UserPreview(profilePicUrl = null, username = "saswat10", userId = "123", fullName = "Saswat Samal"))
        ProfilePreview(UserPreview(profilePicUrl = null, username = "saswat10", userId = "123", fullName = "Saswat Samal"))

    }
}