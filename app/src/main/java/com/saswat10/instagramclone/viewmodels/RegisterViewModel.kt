package com.saswat10.instagramclone.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.saswat10.instagramclone.SnackBarManager
import com.saswat10.instagramclone.models.remote.RemoteUser
import com.saswat10.instagramclone.repository.FirebaseAuthRepository
import com.saswat10.instagramclone.repository.FirestoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface RegisterViewState {
    data class Error(val message: String) : RegisterViewState
    object Loading : RegisterViewState
    data class Success(val user: FirebaseUser?) : RegisterViewState
}


@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: FirebaseAuthRepository,
    private val firestoreRepository: FirestoreRepository
) : ViewModel() {

    private val _viewState = MutableStateFlow<RegisterViewState?>(null)
    val viewState = _viewState.asStateFlow()

    fun register(email: String, password: String, confirmPassword: String) {
        viewModelScope.launch {
            if (email == "" || password == "" || confirmPassword == "") {
                SnackBarManager.showMessage("Fields cannot be empty")
            } else if (password != confirmPassword) {
                SnackBarManager.showMessage("Password don't match")
            } else {
                _viewState.update { RegisterViewState.Loading }
                authRepository.register(email, password)

                    .onSuccess {
                        _viewState.value = RegisterViewState.Success(it)
                        firestoreRepository.createUser(
                            RemoteUser(email = it?.email.toString()),
                            it!!.uid
                        )
                        SnackBarManager.showMessage("Registration Successful")
                    }
                    .onFailure {
                        _viewState.value =
                            RegisterViewState.Error(it.localizedMessage ?: "Unknown error")
                        SnackBarManager.showMessage(it.localizedMessage ?: "Unknown error")
                    }
            }
        }
    }
}