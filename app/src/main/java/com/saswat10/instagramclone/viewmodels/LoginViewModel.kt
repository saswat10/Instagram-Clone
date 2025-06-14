package com.saswat10.instagramclone.viewmodels

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.saswat10.instagramclone.repository.FirebaseAuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: FirebaseAuthRepository
) : ViewModel() {
}

sealed interface LoginState {

}