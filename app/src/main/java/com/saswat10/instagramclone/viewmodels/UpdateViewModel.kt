package com.saswat10.instagramclone.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saswat10.instagramclone.SnackBarManager
import com.saswat10.instagramclone.repository.CloudinaryRepository
import com.saswat10.instagramclone.repository.FirebaseAuthRepository
import com.saswat10.instagramclone.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateViewModel @Inject constructor(
    private val cloudinaryRepository: CloudinaryRepository,
    private val userRepository: UserRepository,
    private val firebaseAuthRepository: FirebaseAuthRepository
) : ViewModel() {

    private val _updateViewState = MutableStateFlow<UpdateViewState?>(null)
    val updateViewState = _updateViewState.asStateFlow()
    val currentUser = firebaseAuthRepository.currentUser!!
    var imageUrl: String? = null

    fun uploadImage(imageFile: Uri, onResult: (String?) -> Unit) {
        cloudinaryRepository.uploadImage(imageFile, "profile_images") { result ->
            result.onSuccess { it ->
                imageUrl = it
                onResult(it)
            }.onFailure {
                _updateViewState.value =
                    UpdateViewState.Error(it.localizedMessage ?: "Unknown error")
                onResult(null)
            }

        }
    }

    fun updateUser(hashMap: HashMap<String, Any>) {
        viewModelScope.launch {
            _updateViewState.update{  UpdateViewState.Loading }
            userRepository.updateUser(currentUser.uid, hashMap)
                .onSuccess{
                    _updateViewState.value = UpdateViewState.Success(it)
                    SnackBarManager.showMessage(it)
                }
                .onFailure {
                    _updateViewState.value =
                        UpdateViewState.Error(it.localizedMessage ?: "Unknown error")
                    SnackBarManager.showMessage(it.localizedMessage ?: "Unknown error")
                }
        }
    }
}


sealed interface UpdateViewState {
    object Loading : UpdateViewState
    data class Success(val message: String) : UpdateViewState
    data class Error(val message: String) : UpdateViewState
}