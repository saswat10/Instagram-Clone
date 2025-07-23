package com.saswat10.instagramclone.presentation.components.posts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.saswat10.instagramclone.presentation.components.common.OutlinedCommentButton
import com.saswat10.instagramclone.presentation.components.common.OutlinedLikeButton
import com.saswat10.instagramclone.presentation.components.user.ImageSizes

@Composable
fun ActionStrip(onComment: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            OutlinedLikeButton()
            OutlinedCommentButton()
        }
        OutlinedButton(
            onComment,
            contentPadding = PaddingValues(horizontal = 2.dp, vertical = 2.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Icon(
                imageVector = Icons.Default.Bookmark,
                contentDescription = "Comment",
                modifier = Modifier.size(ImageSizes.EXTRASMALL)
            )
        }

    }
}
