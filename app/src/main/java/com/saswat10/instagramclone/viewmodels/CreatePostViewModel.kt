package com.saswat10.instagramclone.viewmodels

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saswat10.instagramclone.data.UserDatastoreRepository
import com.saswat10.instagramclone.data.remote.IPostService
import com.saswat10.instagramclone.datastore.UserPreferences
import com.saswat10.instagramclone.domain.use_cases.CreatePostUseCase
import com.saswat10.instagramclone.presentation.components.posts.Media
import com.saswat10.instagramclone.presentation.components.posts.MediaType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val createPostUseCase: CreatePostUseCase,
    private val postService: IPostService,
    private val userPreferencesRepository: UserDatastoreRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreatePostUiState())
    val uiState = _uiState.asStateFlow()

    val userPreferences: StateFlow<UserPreferences?> = userPreferencesRepository.userPreferencesFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )


    fun addCaption(newCaption: String) {
        _uiState.update { it.copy(caption = newCaption) }
    }

    fun addMedia(newMedia: Media, uri: Uri) {
        _uiState.update { it.copy(media = it.media + newMedia, uris = it.uris + uri) }
        Timber.d(_uiState.value.uris.size.toString())
    }

    fun removeMedia(index: Int) {
        _uiState.update {
            if (index in 0 until it.media.size) it.copy(media = it.media - it.media[index]) else it
        }
    }

    fun clearAllMedia() {
        _uiState.update { it.copy(media = emptyList()) }
    }

    fun mimeToMedia(mimeType: String?, index: Int, uri: Uri): Media {
        return if (mimeType?.startsWith("image/") ?: false)
            Media(id = index, url = uri, type = MediaType.IMAGE)
        else
            Media(id = index, url = uri, type = MediaType.VIDEO)
    }

    fun getUriMimeType(context: Context, uri: Uri): String? {
        return context.contentResolver.getType(uri)
    }



    fun share() {
        createPostUseCase.setUploadCallbacks(
            onSuccess = { uploadedUrls->
                Timber.d(uploadedUrls.toString())
            },
            onError = { error ->
                Timber.d(error.description)
            },
            onProgress = { progress ->
            }
        )

        viewModelScope.launch {
            createPostUseCase.uploadFiles(_uiState.value.uris)
            userPreferencesRepository.updateId("11121")
        }

    }
}


data class CreatePostUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val uploadSuccess: Boolean = false,
    val media: List<Media> = emptyList(),
    val caption: String = "",
    val textField: TextFieldState = TextFieldState(),
    val uris: List<Uri> = emptyList()
)