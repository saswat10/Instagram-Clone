package com.saswat10.instagramclone.components.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.saswat10.instagramclone.R

@Composable
fun ProfileCard() {
    Column(modifier = Modifier.fillMaxWidth().padding(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Todo -> replace with async image
            Image(
                painter = painterResource(R.drawable.profile),
                contentDescription = "Profile Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(120.dp)
                    .clip(shape = CircleShape)
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("10", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text("Posts")
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("10K", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text("Followers")
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("1.2K", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text("Following")
            }


        }
        Spacer(Modifier.size(10.dp))
        Text("Kisuke Urahara", fontWeight = FontWeight.Bold)
        Text("Owns a candy shop in the Karakura Town, Loves Cats 🥰🥰🥰😺")


        // display the full name and the bio

    }

}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun PreviewProfileCard() {
    Column(
        modifier = Modifier
            .systemBarsPadding()
            .fillMaxSize()
            .padding(10.dp)
    ) {
        ProfileCard()
    }
}