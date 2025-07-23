package com.saswat10.instagramclone.presentation.components.common

import android.net.Uri
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import coil3.compose.SubcomposeAsyncImage

@Composable
fun Avatar(model: Int? = null, contentDesc: String, size: Dp) {
    if (model == null) Icon(Icons.Default.AccountCircle, contentDesc, Modifier.size(size))
    SubcomposeAsyncImage(
        model = model,
        loading = { CircularProgressIndicator() },
        contentDescription = "Profile Image $contentDesc",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .clip(CircleShape)
            .size(size)
    )
}

@Composable
fun Avatar(model: String? = null, contentDesc: String, size: Dp) {
    if (model == null) Icon(Icons.Default.AccountCircle, contentDesc, Modifier.size(size))
    SubcomposeAsyncImage(
        model = model,
        loading = { CircularProgressIndicator() },
        contentDescription = "Profile Image $contentDesc",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .clip(CircleShape)
            .size(size)
    )
}

@Composable
fun Avatar(model: Uri? = null, contentDesc: String, size: Dp) {
    if (model == null) Icon(Icons.Default.AccountCircle, contentDesc, Modifier.size(size))
    SubcomposeAsyncImage(
        model = model,
        loading = { CircularProgressIndicator() },
        contentDescription = "Profile Image $contentDesc",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .clip(CircleShape)
            .size(size)
    )
}
