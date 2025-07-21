package com.saswat10.instagramclone.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saswat10.instagramclone.domain.repository.IAuthRepository
import com.saswat10.instagramclone.navigation.LoginScreen
import com.saswat10.instagramclone.navigation.MainScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepo: IAuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainActivityUiState())
    val uiState = _uiState.asStateFlow()

    init {
        checkAuthenticationStatus()
    }

    private fun checkAuthenticationStatus() {
        _uiState.update { it.copy(loading = true) }
        viewModelScope.launch {
            val currentUser = authRepo.observeAuthState().first()
            if (currentUser == null) {
                _uiState.update {
                    it.copy(
                        loading = false,
                        startDestination = LoginScreen,
                        showBottomTab = false
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        loading = false,
                        startDestination = MainScreen,
                        showBottomTab = true
                    )
                }
            }
        }
    }
}

data class MainActivityUiState(
    val loading: Boolean = false,
    val startDestination: Any = LoginScreen,
    val showBottomTab: Boolean = false
)