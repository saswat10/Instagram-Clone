package com.saswat10.instagramclone.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saswat10.instagramclone.data.UserDatastoreRepository
import com.saswat10.instagramclone.data.repository.AuthRepository
import com.saswat10.instagramclone.data.repository.UserRepository
import com.saswat10.instagramclone.domain.models.User
import com.saswat10.instagramclone.domain.repository.IAuthRepository
import com.saswat10.instagramclone.domain.repository.IUserRepository
import com.saswat10.instagramclone.repository.FirebaseAuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: IAuthRepository,
    private val userPreferencesRepository: UserDatastoreRepository
) : ViewModel() {

    val user = userRepository.user
    val posts = userRepository.posts

    init {
        viewModelScope.launch {
            val currentUserId = authRepository.observeAuthState()?.uid
            if(currentUserId != null && userRepository.user.value == User()){
                userRepository.getUserById(currentUserId)
                userRepository.getUserPosts(currentUserId).onSuccess {
                    Timber.d("Bew Strigde")
                    Timber.d(posts.toString())
                }.onFailure {
                    Timber.e(it.localizedMessage)
                }
            }

        }
    }

    fun signOut() {
        viewModelScope.launch {
//            authRepository.
            userPreferencesRepository.clearAll()
            userRepository.clearUser()
        }
    }
}