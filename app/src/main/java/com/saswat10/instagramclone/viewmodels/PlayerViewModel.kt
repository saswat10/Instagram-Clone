package com.saswat10.instagramclone.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.saswat10.instagramclone.components.posts.Media
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor() : ViewModel() {
    private val _playerState = MutableStateFlow<ExoPlayer?>(null)
    val playerState: StateFlow<ExoPlayer?> = _playerState
    private val currentPlayingVideo: String? = null
    var player: ExoPlayer? = null
        private set

    private var currentPosition: Long = 0L

    fun initializePlayer(context: Context, media: Media) {
        if (_playerState.value == null) {

            viewModelScope.launch {
                val exoPlayer = ExoPlayer.Builder(context).build().also {
                    val mediaUrl = MediaItem.fromUri(media.url.toString())
                    it.setMediaItem(mediaUrl)
                    it.prepare()
                    it.playWhenReady = true
                    it.repeatMode = ExoPlayer.REPEAT_MODE_ONE
                    it.seekTo(currentPosition)
                    it.addListener(object : Player.Listener {
                        override fun onPlayerError(error: PlaybackException) {
                            handleError(error)
                        }

                        override fun onPlaybackStateChanged(playbackState: Int) {
                            if(playbackState == Player.STATE_ENDED){
                                it.seekTo(0)
                                it.playWhenReady = true
                            }
                        }
                    })
                }
                _playerState.value = exoPlayer
            }
        }
    }
    fun savePlayerState() {
        _playerState.value?.let {
            currentPosition = it.currentPosition
        }
    }

    fun releasePlayer() {
        _playerState.value?.release()
        _playerState.value = null
    }

    private fun handleError(error: PlaybackException) {
        when (error.errorCode) {
            PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED -> {
                // Handle network connection error
                println("Network connection error")
            }

            PlaybackException.ERROR_CODE_IO_FILE_NOT_FOUND -> {
                // Handle file not found error
                println("File not found")
            }

            PlaybackException.ERROR_CODE_DECODER_INIT_FAILED -> {
                // Handle decoder initialization error
                println("Decoder initialization error")
            }

            else -> {
                // Handle other types of errors
                println("Other error: ${error.message}")
            }
        }
    }
}