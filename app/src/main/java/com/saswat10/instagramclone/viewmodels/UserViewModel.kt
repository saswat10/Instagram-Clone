package com.saswat10.instagramclone.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saswat10.instagramclone.data.UserDatastoreRepository
import com.saswat10.instagramclone.datastore.UserPreferences
import com.saswat10.instagramclone.domain.models.UserObs
import com.saswat10.instagramclone.repository.FirebaseAuthRepository
import com.saswat10.instagramclone.repository.UserRepository
import com.saswat10.instagramclone.utils.SnackBarManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface UserViewState {
    object Loading : UserViewState
    data class Success(val user: UserObs) : UserViewState
    data class Error(val exception: Throwable) : UserViewState
}


@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: FirebaseAuthRepository,
    private val userPreferencesRepository: UserDatastoreRepository
) : ViewModel() {
    private val _viewState = MutableStateFlow<UserViewState>(UserViewState.Loading)
    private var _uid = ""
    var imageUrl: String? = null
    val viewState = _viewState.asStateFlow()

    val usersFlow = userRepository.getAllUsersFlow().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList<List<UserObs?>>()
    )

    val userPreferences: StateFlow<UserPreferences?> = userPreferencesRepository.userPreferencesFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    init {
        viewModelScope.launch {
            authRepository.currentUser.let { firebaseUser ->
                if (firebaseUser != null) {
                    _uid = firebaseUser.uid
                    getUser(_uid)
                    getFollowers(_uid)
                    getFollowing(_uid)
                    getPendingAccepts(_uid)
                    getSentRequests(_uid)
                } else {
                    _viewState.value =
                        UserViewState.Error(IllegalArgumentException("User is not logged in"))
                }
            }
        }
    }

    fun getUser(uid: String = _uid) {
        if (uid.isEmpty()) {
            _viewState.value =
                UserViewState.Error(IllegalStateException("User ID is not set. Cannot fetch followers."))
            return
        }
        viewModelScope.launch {
            _viewState.value = UserViewState.Loading
            userRepository.getUserByUid(uid).onSuccess {
                if (it != null) {
                    _viewState.value = UserViewState.Success(it)
                    imageUrl = it.profilePic
                } else {
                    _viewState.value =
                        UserViewState.Error(NoSuchElementException("User Not Found"))
                }
            }.onFailure {
                _viewState.value = UserViewState.Error(it)
            }
        }
    }

    fun updateUser(hashMap: HashMap<String, Any>) {
        viewModelScope.launch {
            _viewState.update { UserViewState.Loading }
            userRepository.updateUser(_uid, hashMap)
                .onSuccess {
                    SnackBarManager.showMessage(it)
                }
                .onFailure {
                    _viewState.value =
                        UserViewState.Error(Error(it.localizedMessage ?: "Unknown error"))
                    SnackBarManager.showMessage(it.localizedMessage ?: "Unknown error")
                }
        }
    }


    fun signOut() {
        viewModelScope.launch {
            authRepository.signOut()
            userPreferencesRepository.clearAll()
        }
    }

    fun getFollowers(uid: String = _uid) {

        if (uid.isEmpty()) {
            _viewState.value =
                UserViewState.Error(IllegalStateException("User ID is not set. Cannot fetch followers."))
            return
        }
        viewModelScope.launch {
            _viewState.value = UserViewState.Loading
            userRepository.getFollowers(uid).onSuccess { followersList ->
                _viewState.update { state ->
                    when (state) {
                        is UserViewState.Success -> {
                            state.copy(user = state.user.copy(followers = followersList))
                        }

                        else -> {
                            state
                        }
                    }
                }
            }.onFailure {

            }
        }
    }

    fun getFollowing(uid: String = _uid) {
        if (uid.isEmpty()) {
            _viewState.value =
                UserViewState.Error(IllegalStateException("User ID is not set. Cannot fetch followers."))
            return
        }
        viewModelScope.launch {
            _viewState.value = UserViewState.Loading
            userRepository.getFollowing(uid).onSuccess { followingsList ->
                _viewState.update { state ->
                    when (state) {
                        is UserViewState.Success -> {
                            state.copy(user = state.user.copy(following = followingsList))
                        }

                        else -> {
                            state
                        }
                    }
                }
            }.onFailure {

            }
        }
    }

    fun getPendingAccepts(uid: String = _uid) {
        if (uid.isEmpty()) {
            _viewState.value =
                UserViewState.Error(IllegalStateException("User ID is not set. Cannot fetch followers."))
            return
        }
        viewModelScope.launch {
            _viewState.value = UserViewState.Loading
            userRepository.getPendingRequests(uid).onSuccess { pendingRequests ->
                _viewState.update { state ->
                    when (state) {
                        is UserViewState.Success -> {
                            state.copy(user = state.user.copy(pendingRequests = pendingRequests))
                        }

                        else -> {
                            state
                        }
                    }
                }
            }.onFailure {

            }
        }
    }

    fun getSentRequests(uid: String = _uid) {
        if (uid.isEmpty()) {
            _viewState.value =
                UserViewState.Error(IllegalStateException("User ID is not set. Cannot fetch followers."))
            return
        }
        viewModelScope.launch {
            _viewState.value = UserViewState.Loading

            userRepository.getSentRequests(uid).onSuccess { sentRequests ->
                _viewState.update { state ->
                    when (state) {
                        is UserViewState.Success -> {
                            state.copy(user = state.user.copy(sentRequests = sentRequests))
                        }

                        else -> {
                            state
                        }
                    }
                }
            }.onFailure {

            }
        }
    }

    fun acceptFollowRequest(uid: String = _uid, targetUid: String) {
        if (uid.isEmpty()) {
            _viewState.value =
                UserViewState.Error(IllegalStateException("User ID is not set. Cannot fetch followers."))
            return
        }
        viewModelScope.launch {
            userRepository.acceptRequest(uid, targetUid).onSuccess {
                SnackBarManager.showMessage("Accepted")
            }
                .onFailure {
                    SnackBarManager.showMessage(it.localizedMessage ?: "Unknown Error Occurred")
                }
        }
    }

    fun sendFollowRequest(uid: String = _uid, targetUid: String) {
        if (uid.isEmpty()) {
            _viewState.value =
                UserViewState.Error(IllegalStateException("User ID is not set. Cannot fetch followers."))
            return
        }
        viewModelScope.launch {
            userRepository.followUser(uid, targetUid).onSuccess {
                SnackBarManager.showMessage("Accepted")
            }
                .onFailure {
                    SnackBarManager.showMessage(it.localizedMessage ?: "Unknown Error Occurred")
                }
        }
    }

    fun unfollowUser(uid: String = _uid, targetUid: String) {
        if (uid.isEmpty()) {
            _viewState.value =
                UserViewState.Error(IllegalStateException("User ID is not set. Cannot fetch followers."))
            return
        }
        viewModelScope.launch {
            userRepository.unfollowUser(uid, targetUid).onSuccess {
                SnackBarManager.showMessage("Accepted")
            }
                .onFailure {
                    SnackBarManager.showMessage(it.localizedMessage ?: "Unknown Error Occurred")
                }
        }
    }

    fun declineRequest(uid: String = _uid, targetUid: String) {
        if (uid.isEmpty()) {
            _viewState.value =
                UserViewState.Error(IllegalStateException("User ID is not set. Cannot fetch followers."))
            return
        }
        viewModelScope.launch {
            userRepository.declineRequest(uid, targetUid).onSuccess {
                SnackBarManager.showMessage("Accepted")
            }
                .onFailure {
                    SnackBarManager.showMessage(it.localizedMessage ?: "Unknown Error Occurred")
                }
        }
    }

    fun withdrawRequest(uid: String = _uid, targetUid: String) {
        if (uid.isEmpty()) {
            _viewState.value =
                UserViewState.Error(IllegalStateException("User ID is not set. Cannot fetch followers."))
            return
        }
        viewModelScope.launch {
            userRepository.withdrawFollowRequest(uid, targetUid).onSuccess {
                SnackBarManager.showMessage("Accepted")
            }
                .onFailure {
                    SnackBarManager.showMessage(it.localizedMessage ?: "Unknown Error Occurred")
                }
        }
    }


}

