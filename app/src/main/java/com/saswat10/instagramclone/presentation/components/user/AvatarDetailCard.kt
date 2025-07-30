package com.saswat10.instagramclone.presentation.components.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.saswat10.instagramclone.domain.models.User
import com.saswat10.instagramclone.presentation.components.common.Avatar

@Composable
fun AvatarDetailCard(user: User, onEdit: (() -> Unit)? = null, onSignOut: (() -> Unit)? = null) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(
                40.dp,
                alignment = Alignment.CenterHorizontally
            )
        ) {
            // Todo -> replace with async image
            Avatar(
                model = user.profilePic,
                contentDesc = "Profile Image",
                size = 100.dp
            )
            Column {
                Text(
                    user.name,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(user.getCreatedAt(), style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.size(10.dp))
            }
            Column {
                if (onEdit != null) {
                    FilledIconButton(onClick = onEdit) {
                        Icon(Icons.Default.Edit, "Edit", Modifier.size(ImageSizes.EXTRASMALL))
                    }
                }
                if (onSignOut != null) {
                    FilledTonalIconButton(
                        onClick = onSignOut,
                        colors = IconButtonDefaults.filledTonalIconButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.Logout,
                            "Edit",
                            Modifier.size(ImageSizes.EXTRASMALL)
                        )
                    }
                }
            }
        }
        Spacer(Modifier.size(10.dp))
        Text(user.bio, Modifier.fillMaxWidth().padding(6.dp))
        Spacer(Modifier.size(10.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            PostFriendsNumber(user.friends, "Friends", Modifier.weight(1f))
            PostFriendsNumber(user.posts, "Posts", Modifier.weight(1f))
        }
    }

}


@Composable
fun PostFriendsNumber(number: Int, value: String, modifier: Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier
            .background(
                MaterialTheme.colorScheme.outlineVariant, shape = MaterialTheme.shapes.medium
            )
            .padding(2.dp)
    ) {
        Text(
            "$number",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.W900
        )
        Text(value)
    }
}