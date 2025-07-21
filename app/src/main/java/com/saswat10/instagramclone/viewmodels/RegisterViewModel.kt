package com.saswat10.instagramclone.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.saswat10.instagramclone.models.remote.RemoteUser
import com.saswat10.instagramclone.repository.CloudinaryRepository
import com.saswat10.instagramclone.repository.FirebaseAuthRepository
import com.saswat10.instagramclone.repository.UserRepository
import com.saswat10.instagramclone.utils.SnackBarManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: FirebaseAuthRepository,
    private val userRepository: UserRepository,
    private val cloudinaryRepository: CloudinaryRepository
) : ViewModel() {

    private val _viewState = MutableStateFlow(RegisterFormUiState())
    val viewState = _viewState.asStateFlow()

    fun registerAndCreate() {
        _viewState.update { it.copy(uiState = UiState.Loading) }
        viewModelScope.launch {
            // first register
            authRepository.register(
                email = viewState.value.email,
                password = viewState.value.password
            ).onSuccess { firebaseUser ->
                // create user
                val user = RemoteUser(
                    uid = firebaseUser!!.uid,
                    username = viewState.value.username,
                    fullName = viewState.value.fullName,
                    email = viewState.value.email
                )
                userRepository.createUser(uid = firebaseUser.uid, user = user)
                    .onSuccess { message ->
                        _viewState.update {
                            it.copy(
                                uiState = UiState.Success(
                                    "User created successfully",
                                    firebaseUser
                                )
                            )
                        }
                        SnackBarManager.showMessage("User Creation Success")
                    }.onFailure { error ->
                    _viewState.update { it.copy(uiState = UiState.Error("Error creating user")) }
                    SnackBarManager.showMessage(error.localizedMessage ?: "Unknown Error")
                }
            }.onFailure { error ->
                _viewState.update { it.copy(uiState = UiState.Error("Error registering user")) }
                SnackBarManager.showMessage(error.localizedMessage ?: "Unknown Error")
            }

        }
    }


    fun onUsernameChange(username: String) {
        _viewState.update {
            it.copy(
                username = username
            )
        }
    }

    fun onFullNameChange(fullName: String) {
        _viewState.update {
            it.copy(fullName = fullName)
        }
    }

    fun onEmailChange(email: String) {
        _viewState.update {
            it.copy(email = email)
        }
    }

    fun onPasswordChange(password: String) {
        _viewState.update {
            it.copy(password = password)
        }
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        _viewState.update {
            it.copy(confirmPassword = confirmPassword)
        }
    }

}

data class RegisterFormUiState(
    val username: String = "",
    val fullName: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val profilePicture: Uri? = null,
    val uiState: UiState? = null
)

sealed interface UiState {
    object Loading : UiState
    data class Success(val message: String, val user: FirebaseUser?) : UiState
    data class Error(val message: String) : UiState
}