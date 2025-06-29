package com.saswat10.instagramclone.components.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.saswat10.instagramclone.models.domain.User

@Composable
fun ProfileCard(userPreview: User, onClickFollow:()->Unit) {
    OutlinedCard(shape = MaterialTheme.shapes.small) {
        Column(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ProfileImage(userPreview.profilePic, ImageSizes.SMALL)
            Text(
                text = userPreview.getname(),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
            Button(onClick = {onClickFollow()}, modifier = Modifier.fillMaxWidth(),) {
                Text(text = "Follow")
            }
        }
    }
}