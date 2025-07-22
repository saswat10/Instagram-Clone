package com.saswat10.instagramclone.presentation.components.posts

import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.saswat10.instagramclone.viewmodels.PlayerViewModel

private const val VIDEO_URL = "https://cdn.pixabay.com/video/2015/08/20/468-136808389_large.mp4"

@Composable
fun Media3PlayerView(videoUrl: Media, playerViewModel: PlayerViewModel = hiltViewModel(), modifier: Modifier) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val player by playerViewModel.playerState.collectAsState()

    LaunchedEffect(videoUrl) {
        playerViewModel.initializePlayer(context, videoUrl)
    }

    DisposableEffect(lifecycleOwner) {
        onDispose {
            playerViewModel.savePlayerState()
            playerViewModel.releasePlayer()
        }
    }

    Column(modifier = modifier, verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Media3AndroidView(player)
    }

}


@OptIn(UnstableApi::class)
@Composable
fun Media3AndroidView(player: ExoPlayer?) {
    AndroidView(
        modifier = Modifier.fillMaxWidth().background(Color.Black),
        factory = { context ->

            PlayerView(context).apply {
                useController = true
                setShowNextButton(false)
                setShowPreviousButton(false)
                setShowRewindButton(false)
                setShowFastForwardButton(false)
                setShowVrButton(false)
                this.player = player
            }
        },
        update = { playerView ->
            playerView.player = player
        }
    )
}

@Composable
fun PlayerControls(player: ExoPlayer?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = { player?.playWhenReady = true }) {
            Text("Play")
        }
        Button(onClick = { player?.playWhenReady = false }) {
            Text("Pause")
        }

        Button(onClick = {
            player?.seekTo(player.currentPosition - 10_000) // Seek backward 10 seconds
        }) {
            Text("Seek -10s")
        }
        Button(onClick = {
            player?.seekTo(player.currentPosition + 10_000) // Seek forward 10 seconds
        }) {
            Text("Seek +10s")
        }
    }
}
