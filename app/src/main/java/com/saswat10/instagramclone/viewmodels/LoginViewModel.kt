package com.saswat10.instagramclone.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.saswat10.instagramclone.SnackBarManager
import com.saswat10.instagramclone.repository.FirebaseAuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface LoginViewState {
    object Loading : LoginViewState
    data class Error(val message: String) : LoginViewState
    data class Success(val user: FirebaseUser?) : LoginViewState
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: FirebaseAuthRepository
) : ViewModel() {

    private val _viewState = MutableStateFlow<LoginViewState?>(null)
    val viewState = _viewState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            if (email == "" || password == "") {
                _viewState.update {
                    LoginViewState.Error(
                        message = "Email and password cannot be empty"
                    )
                }
                SnackBarManager.showMessage("Email and password cannot be empty.")
            } else {

                _viewState.update { LoginViewState.Loading }
                authRepository.signIn(email, password).onSuccess { user ->
                    _viewState.update {
                        LoginViewState.Success(user = user)
                    }
                }.onFailure { failure ->
                    _viewState.update { it ->
                        LoginViewState.Error(message = failure.localizedMessage ?: "Unknown error")

                    }
                    SnackBarManager.showMessage(failure.localizedMessage ?: "Unknown error")
                }
            }
        }
    }

}

