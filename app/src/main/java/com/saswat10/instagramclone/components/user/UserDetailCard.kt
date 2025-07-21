package com.saswat10.instagramclone.components.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import com.saswat10.instagramclone.domain.models.User

@Composable
fun UserDetailCard(user: User) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Todo -> replace with async image
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(user.profilePic)
                    .listener(
                        onStart = { println("Coil: Image loading started") },
                        onSuccess = { request, result -> println("Coil: Image loaded successfully: ${request.data}") },
                        onError = { request, result -> println("Coil: Image loading failed for ${request.data}: ${result.throwable?.message}") }
                    )
                    .build(),

                contentDescription = "Profile Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(120.dp)
                    .clip(shape = CircleShape),
                loading = {
                    CircularProgressIndicator()
                }
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "${user.posts}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text("Posts")
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "${user.followerCount}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text("Followers")
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "${user.followingCount}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text("Following")
            }


        }
        Spacer(Modifier.size(10.dp))
        Text(user.fullName, fontWeight = FontWeight.Bold)
        Text(user.bio)


        // display the full name and the bio

    }

}

