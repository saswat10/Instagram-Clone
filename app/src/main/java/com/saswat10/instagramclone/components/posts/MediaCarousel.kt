package com.saswat10.instagramclone.components.posts

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.saswat10.instagramclone.R

data class Media(
    val id: String,
    val url: Int, // change this when using for cloud images/video
    val type: MediaType
    // add content description
)
data class MediaUri(
    val id: String,
    val url: Uri, // change this when using for cloud images/video
    val type: MediaType
    // add content description
)

enum class MediaType(
    val type: String
) {
    IMAGE("image"),
    VIDEO("video")
}


val mediaList = listOf<Media>(
    Media("1", R.drawable.img1, MediaType.IMAGE),
    Media("2", R.drawable.profile, MediaType.IMAGE),
    Media("3", R.drawable.download, MediaType.IMAGE),
    Media("4", R.drawable.astronaut_nord, MediaType.IMAGE)
)

@Composable
fun MediaCarousel(mediaList: List<MediaUri> = emptyList()) {
    val pagerState = rememberPagerState(
        pageCount = {
            mediaList.size
        },
    )

    var isVisible by remember { mutableStateOf(false) }
    HorizontalPager(state = pagerState, Modifier.fillMaxWidth()) { page ->
        val media = mediaList[page]
        Box() {
            AsyncImage(
                model = media.url,
                contentDescription = null,
                modifier = Modifier
                    .height(300.dp)
                    .fillMaxWidth()
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = { isVisible = !isVisible },
                            onPress = {},
                            onDoubleTap = {},
                            onLongPress = {}
                        )
                    },
                contentScale = ContentScale.Crop
            )
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier.align(Alignment.BottomEnd)
            ) {
                Text(
                    text = "${page + 1}/${pagerState.pageCount}",
                    modifier = Modifier
                        .padding(10.dp)
                        .clip(ShapeDefaults.Small)
                        .background(MaterialTheme.colorScheme.background.copy(alpha = 0.5f))
                        .padding(6.dp)


                )
            }
        }
    }
}