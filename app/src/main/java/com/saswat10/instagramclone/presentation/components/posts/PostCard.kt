package com.saswat10.instagramclone.presentation.components.posts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.saswat10.instagramclone.presentation.components.common.AvatarHeader

@Composable
fun PostCard(onComment: () -> Unit = {}, mediaList: List<Media>) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        AvatarHeader()
        Caption()
        MediaCarousel2(mediaList)
        ActionStrip(onComment)
        HorizontalDivider()
    }
}
