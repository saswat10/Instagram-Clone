package com.saswat10.instagramclone.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saswat10.instagramclone.data.UserDatastoreRepository
import com.saswat10.instagramclone.data.repository.UserRepository
import com.saswat10.instagramclone.domain.models.User
import com.saswat10.instagramclone.domain.repository.IAuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: IAuthRepository,
    private val userPreferencesRepository: UserDatastoreRepository
): ViewModel() {


    init {
        viewModelScope.launch {
            val currentUserId = authRepository.observeAuthState()?.uid
            if(currentUserId != null && userRepository.user.value == User()){
                userRepository.getUserById(currentUserId)
            }
        }
    }

    val user = userRepository.user

}

data class ProfileUiState(
    val loading: Boolean = false,
    val success: Boolean? = null,
    val errorMessage: String? = null,
    val name: String = "",
    val username: String = "",
    val profilePic: String = "",
    val bio: String = ""
)