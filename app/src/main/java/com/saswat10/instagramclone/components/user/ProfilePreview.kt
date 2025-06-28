package com.saswat10.instagramclone.components.user

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.saswat10.instagramclone.models.domain.User.UserPreview
import timber.log.Timber

@Composable
fun ProfilePreview(userPreview: UserPreview) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .clickable { Timber.d("Full Click") }) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // display icon
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (userPreview.profilePic.isNullOrBlank()) Icon(
                    imageVector = Icons.Rounded.AccountCircle,
                    contentDescription = "Profile Image",
                    modifier = Modifier.size(40.dp)
                )
                else
                    AsyncImage(
                        model = userPreview.profilePic,
                        contentDescription = "Profile Image"
                    )
                Column() {
                    // user name
                    Text("@${userPreview.username}", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                    // full name
                    Text(userPreview.fullName, style = MaterialTheme.typography.bodyMedium)
                }


            }
            // follow, message button
            TextButton(onClick = { Timber.d("Button Click") }) {
                Text("Follow")
            }
        }
    }
}


