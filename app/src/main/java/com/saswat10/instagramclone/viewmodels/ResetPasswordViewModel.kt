package com.saswat10.instagramclone.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saswat10.instagramclone.SnackBarManager
import com.saswat10.instagramclone.repository.FirebaseAuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface ResetPasswordViewState {
    object Loading : ResetPasswordViewState
    object Success : ResetPasswordViewState
    object Error : ResetPasswordViewState
}
@HiltViewModel
class ResetPasswordViewModel @Inject constructor(private val authRepository: FirebaseAuthRepository) :
    ViewModel() {

    private val _viewState = MutableStateFlow<ResetPasswordViewState?>(null)
    val viewState = _viewState.asStateFlow()

    fun resetPassword(email: String) {
        viewModelScope.launch {
            _viewState.value = ResetPasswordViewState.Loading
            authRepository.resetPassword(email).onSuccess {
                _viewState.value = ResetPasswordViewState.Success
                SnackBarManager.showMessage("Password reset email sent")
            }.onFailure {
                _viewState.value = ResetPasswordViewState.Error
                SnackBarManager.showMessage(it.localizedMessage ?: "Unknown error")
            }
        }
    }

}