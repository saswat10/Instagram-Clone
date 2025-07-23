package com.saswat10.instagramclone.presentation.components.posts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saswat10.instagramclone.R
import com.saswat10.instagramclone.presentation.components.common.Avatar
import com.saswat10.instagramclone.presentation.components.user.ImageSizes

@Composable
fun Comment() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.Top
    ) {
        Avatar(R.drawable.download, "", ImageSizes.SMALL)
        Column(
            modifier = Modifier.padding(horizontal = 10.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "@saswat10", fontWeight = FontWeight.W900, fontSize = 14.sp)
                Text(
                    text = "•",
                    fontSize = 14.sp
                )
                Text(text = "24h ago", color = Color.Gray, fontSize = 14.sp)
            }
            Text(
                "This is a comment!, In case we have a very large comment what will happen to this thread",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
