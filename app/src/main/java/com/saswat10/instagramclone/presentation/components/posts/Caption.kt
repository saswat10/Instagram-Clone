package com.saswat10.instagramclone.presentation.components.posts

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun Caption() {
    Text(
        text="This is My First Here on Instagram Clone, Hope everyone starts posting on this platform ver soon!!",
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
        fontWeight = FontWeight.Bold
    )
}