package com.saswat10.instagramclone.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.saswat10.instagramclone.data.UserDatastoreRepository
import com.saswat10.instagramclone.datastore.UserPreferences
import com.saswat10.instagramclone.domain.models.User
import com.saswat10.instagramclone.domain.repository.IAuthRepository
import com.saswat10.instagramclone.domain.use_cases.LoginUseCase
import com.saswat10.instagramclone.repository.FirebaseAuthRepository
import com.saswat10.instagramclone.utils.SnackBarManager
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
    val shouldNavigate: Boolean = false,
    val user: User? = null
)


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: FirebaseAuthRepository,
    private val authRepo: IAuthRepository,
    private val loginUseCase: LoginUseCase,
    private val userPreferencesRepository: UserDatastoreRepository

) : ViewModel() {

    private val _viewState = MutableStateFlow<LoginViewState?>(null)
    val viewState = _viewState.asStateFlow()


    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    val userPreferences: StateFlow<UserPreferences?> = userPreferencesRepository.userPreferencesFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )



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

            loginUseCase(email, password).onSuccess { user ->

               user?.let {
                   Timber.d(user.toString())
                   _uiState.update { loginUiState ->
                       loginUiState.copy(
                           isLoginSuccess = true,
                           loading = false,
                           shouldNavigate = true,
                           error = null,
                           user = user
                       )
                   }
                   userPreferencesRepository.updateName(user.name)
                   userPreferencesRepository.updateId(user.userId)
                   userPreferencesRepository.updateUsername(user.username)
                   userPreferencesRepository.updateProfilePic(user.profilePic)
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
}

