package com.saswat10.instagramclone.presentation.components.user

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

object ImageSizes {
    val LARGE = 60.dp
    val MEDIUM = 40.dp
    val SMALL = 80.dp
}


@Composable
fun ProfileImage(profilePic: String?, imageSize: Dp) {
    if (profilePic.isNullOrBlank()) Icon(
        imageVector = Icons.Rounded.AccountCircle,
        contentDescription = "Profile Image",
        modifier = Modifier.size(imageSize)
    )
    else
        AsyncImage(
            model = profilePic,
            contentDescription = "Profile Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(imageSize).clip(CircleShape)
        )
}