package com.saswat10.instagramclone.presentation.components.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Comment
import androidx.compose.material.icons.rounded.Comment
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.saswat10.instagramclone.presentation.components.user.ImageSizes

@Composable
fun OutlinedCommentButton() {
    OutlinedButton(
        onClick = {},
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 2.dp),
        shape = MaterialTheme.shapes.medium

    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                Icons.AutoMirrored.Rounded.Comment, "Comment", modifier = Modifier.size(
                    ImageSizes.EXTRASMALL
                )
            )
            Text("14")
        }
    }
}