package com.saswat10.instagramclone.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.saswat10.instagramclone.domain.repository.IAuthRepository
import com.saswat10.instagramclone.domain.use_cases.LoginUseCase
import com.saswat10.instagramclone.repository.FirebaseAuthRepository
import com.saswat10.instagramclone.utils.SnackBarManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface LoginViewState {
    object Loading : LoginViewState
    data class Error(val message: String) : LoginViewState
    data class Success(val user: FirebaseUser?) : LoginViewState
}


data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val loading: Boolean = false,
    val error: String? = null,
    val showPassword: Boolean = false,
    val isLoginSuccess: Boolean = false,
    val shouldNavigate: Boolean = false
)


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: FirebaseAuthRepository,
    private val authRepo: IAuthRepository,
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _viewState = MutableStateFlow<LoginViewState?>(null)
    val viewState = _viewState.asStateFlow()


    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()


    fun onEmailChanged(newEmail: String) {
        _uiState.update { it.copy(email = newEmail) }
    }

    fun onPasswordChanged(newPassword: String) {
        _uiState.update { it.copy(password = newPassword) }
    }

    fun toggleShowPassword() {
        _uiState.update {
            it.copy(showPassword = !it.showPassword)
        }
    }

    fun onLoginClicked() {
        _uiState.update { it.copy(loading = true) }
        viewModelScope.launch {
            val email = _uiState.value.email
            val password = _uiState.value.password

            val result = loginUseCase(email, password)
            result.onSuccess { user ->
                _uiState.update { loginUiState ->
                    loginUiState.copy(
                        isLoginSuccess = user != null,
                        loading = false,
                        shouldNavigate = user != null,
                        error = if (user == null) "Login Success but user profile not found" else null
                    )
                }
            }.onFailure { error ->
                _uiState.update { loginUiState ->
                    loginUiState.copy(
                        isLoginSuccess = false,
                        loading = false,
                        error = error.localizedMessage ?: "Unknown Error occurred"
                    )
                }
            }
        }
    }


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

