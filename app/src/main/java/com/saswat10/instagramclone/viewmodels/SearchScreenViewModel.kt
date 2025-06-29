package com.saswat10.instagramclone.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saswat10.instagramclone.models.domain.User
import com.saswat10.instagramclone.repository.FirebaseAuthRepository
import com.saswat10.instagramclone.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SearchScreenViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: FirebaseAuthRepository
) : ViewModel() {

    val currentUser = authRepository.currentUser!!
    private val _viewState = MutableStateFlow(SearchScreenViewState())
    val viewState: StateFlow<SearchScreenViewState> = _viewState.asStateFlow()

    init {
        getUsers()
    }

    fun getUsers() {
        viewModelScope.launch {
            _viewState.value = _viewState.value.copy(isLoading = true)
            val result = userRepository.getAllUsers().onSuccess {
                _viewState.value = _viewState.value.copy(
                    users = it.filter { user ->
                        user?.userId != currentUser.uid
                    },
                    isLoading = false
                )
            }.onFailure {
                _viewState.value = _viewState.value.copy(
                    error = it.localizedMessage,
                    isLoading = false
                )
            }

        }
    }

    fun followUser(targetUid: String) {
        viewModelScope.launch {
            val result = userRepository.followUser(currentUser.uid, targetUid= targetUid).onSuccess {
                _viewState.value = _viewState.value.copy(
                    users = _viewState.value.users.filter {
                        it?.userId != targetUid
                    }
                )
            }.onFailure {
                Timber.d(it.localizedMessage)
            }
        }

    }
}

data class SearchScreenViewState(
    val users: List<User?> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)