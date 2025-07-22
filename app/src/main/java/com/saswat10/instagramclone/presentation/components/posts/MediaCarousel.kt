package com.saswat10.instagramclone.presentation.components.posts

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.SubcomposeAsyncImage
import com.saswat10.instagramclone.R

data class Media(
    val id: String,
    val url: Any, // change this when using for cloud images/video
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


val mediaList = listOf(
    Media("1", R.drawable.img1, MediaType.IMAGE),
    Media(
        "2",
        "https://packaged-media.redd.it/ja9e4qlmjsdf1/pb/m2-res_720p.mp4?m=DASHPlaylist.mpd&v=1&e=1752930000&s=645564a713aff9c8af99fcf4009314123b3c8b57",
        MediaType.VIDEO
    ),
    Media("3", "https://i.redd.it/tsgrtjdmbwcf1.png", MediaType.IMAGE),
    Media("4", R.drawable.astronaut_nord, MediaType.IMAGE),
    Media("5", "https://i.redd.it/km3fsv43vndf1.png", MediaType.IMAGE)
)

val mediaList2 = listOf(
    Media("1", "https://i.redd.it/inp5g0gawmdf1.png", MediaType.IMAGE),
    Media(
        "2",
        "https://packaged-media.redd.it/vp4qybt64qdf1/pb/m2-res_450p.mp4?m=DASHPlaylist.mpd&v=1&e=1752930000&s=0a2c217f3662e47a4d93d3e734c4194fea7abe2a",
        MediaType.VIDEO
    ),
    Media("3", "https://i.redd.it/tsgrtjdmbwcf1.png", MediaType.IMAGE),
)

val mediaList3 = listOf(
    Media("1", "https://i.redd.it/05lk0ri3fpdf1.jpeg", MediaType.IMAGE),
    Media("3", R.drawable.img1, MediaType.IMAGE),
    Media(
        "2",
        "https://packaged-media.redd.it/lazq39khlqdf1/pb/m2-res_640p.mp4?m=DASHPlaylist.mpd&v=1&e=1752933600&s=728ffaa6bf21695b57357763eb6d7f6a6709889d",
        MediaType.VIDEO
    ),
    Media("4", R.drawable.astronaut_nord, MediaType.IMAGE),
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
        Box(
            modifier = Modifier
                .aspectRatio(1f, matchHeightConstraintsFirst = false)
                .fillMaxWidth()
        ) {
            SubcomposeAsyncImage(
                model = media.url,
                loading = { CircularProgressIndicator() },
                contentDescription = null,
                modifier = Modifier
                    .aspectRatio(1f)
                    .matchParentSize()
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = { isVisible = !isVisible },
                            onPress = {},
                            onDoubleTap = {},
                            onLongPress = {}
                        )
                    },
                contentScale = ContentScale.Inside
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


@Composable
fun MediaCarousel2(mediaList: List<Media> = emptyList()) {
    val pagerState = rememberPagerState(
        pageCount = {
            mediaList.size
        },
    )

    var isVisible by remember { mutableStateOf(false) }
    HorizontalPager(state = pagerState, Modifier.fillMaxWidth()) { page ->
        val media = mediaList[page]
        val type = mediaList[page].type
        Box(modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)) {
            when (type) {
                MediaType.IMAGE -> {
                    AsyncImage(
                        model = media.url,
                        contentDescription = null,
                        modifier = Modifier
//                            .aspectRatio(1f)
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
                }

                MediaType.VIDEO -> {
                    Media3PlayerView(
                        media,
                        modifier = Modifier
                            .matchParentSize()
                            .background(Color.Black)
                            .align(Alignment.Center)
                    )
                }
            }

            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier.align(Alignment.BottomEnd)
            ) {
                Text(
                    text = "${page + 1}/${pagerState.pageCount}",
                    color = Color.White,
                    modifier = Modifier
                        .padding(10.dp)
                        .clip(ShapeDefaults.Small)
                        .background(Color.Black.copy(alpha = 0.4f))
                        .padding(6.dp)


                )
            }
        }
    }
}