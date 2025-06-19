package com.saswat10.instagramclone.viewmodels

import androidx.lifecycle.ViewModel
import com.saswat10.instagramclone.repository.FirebaseAuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UpdateViewModel @Inject constructor(private val firebaseAuthRepository: FirebaseAuthRepository):
    ViewModel() {

    fun signOut() {
        firebaseAuthRepository.signOut()
    }

}