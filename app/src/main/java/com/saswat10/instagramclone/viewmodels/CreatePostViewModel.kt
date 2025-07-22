package com.saswat10.instagramclone.viewmodels

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import com.saswat10.instagramclone.presentation.components.posts.Media
import com.saswat10.instagramclone.presentation.components.posts.MediaType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(CreatePostUiState())
    val uiState = _uiState.asStateFlow()


    fun addCaption(newCaption: String) {
        _uiState.update { it.copy(caption = newCaption) }
    }

    fun addMedia(newMedia: Media) {
        _uiState.update { it.copy(media = it.media + newMedia) }
    }

    fun removeMedia(index: Int) {
        _uiState.update {
            if (index in 0 until it.media.size) it.copy(media = it.media - it.media[index]) else it
        }
    }

    fun clearAllMedia(){
        _uiState.update { it.copy(media = emptyList()) }
    }

    fun mimeToMedia(mimeType: String?, index: Int, uri: Uri): Media{
        return if(mimeType?.startsWith("image/") ?: false)
            Media(id = index, url = uri, type = MediaType.IMAGE)
        else
            Media(id = index, url = uri, type = MediaType.VIDEO)
    }

    fun getUriMimeType(context: Context, uri: Uri): String? {
        return context.contentResolver.getType(uri)
    }

}


data class CreatePostUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val uploadSuccess: Boolean = false,
    val media: List<Media> = emptyList(),
    val caption: String = "",
    val textField: TextFieldState = TextFieldState()
)