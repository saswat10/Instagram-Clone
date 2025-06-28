package com.saswat10.instagramclone.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saswat10.instagramclone.repository.CloudinaryRepository
import com.saswat10.instagramclone.repository.FirebaseAuthRepository
import com.saswat10.instagramclone.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UpdateViewModel @Inject constructor(
    private val cloudinaryRepository: CloudinaryRepository,
    private val userRepository: UserRepository,
    private val firebaseAuthRepository: FirebaseAuthRepository
) : ViewModel() {

    private val _updateViewState = MutableStateFlow(UpdateProfileUiState())
    val updateViewState = _updateViewState.asStateFlow()

    // It's safer to use `currentUser` as nullable or handle its nullability explicitly.
    // If you're certain it won't be null after login, you can keep the `!!`,
    // but often it's better to guard against it.
    private val currentUser =
        firebaseAuthRepository.currentUser // Remove `!!` here, handle null later

    // For one-time events like showing Snackbars or triggering navigation
    private val _eventFlow = MutableSharedFlow<UpdateProfileEvent>()
    val eventFlow = _eventFlow.asSharedFlow()


    init {
        getUser()
    }

    fun getUser() { // Removed default parameter as currentUser is a ViewModel property
        val currentUserId = currentUser?.uid
        if (currentUserId == null) {
            _updateViewState.update {
                it.copy(
                    errorMessage = "User not logged in.",
                    isLoading = false
                )
            }
            // Consider emitting an event for this critical error, as it prevents loading data
            viewModelScope.launch { _eventFlow.emit(UpdateProfileEvent.ShowSnackbar("User not logged in.")) }
            return
        }

        viewModelScope.launch {
            _updateViewState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null,
                    successMessage = null
                )
            } // Correct update
            userRepository.getUserByUid(currentUserId).onSuccess { userResult ->
                if (userResult != null) {
                    _updateViewState.update { currentState -> // Use `update` and `it` or `currentState`
                        currentState.copy(
                            username = userResult.username,
                            fullName = userResult.fullName, // Make sure User data class has fullName
                            profilePictureUrl = userResult.profilePic, // Make sure User data class has profilePic
                            bio = userResult.bio,
                            isLoading = false
                        )
                    }
                } else {
                    _updateViewState.update {
                        it.copy(
                            errorMessage = "User data not found.",
                            isLoading = false,
                        )
                    }
                    viewModelScope.launch { _eventFlow.emit(UpdateProfileEvent.ShowSnackbar("User data not found.")) }
                }
            }.onFailure { e ->
                _updateViewState.update {
                    it.copy(
                        errorMessage = e.localizedMessage ?: "Failed to load profile.",
                        isLoading = false,
                    )
                }
                viewModelScope.launch {
                    _eventFlow.emit(
                        UpdateProfileEvent.ShowSnackbar(
                            e.localizedMessage ?: "Failed to load profile."
                        )
                    )
                }
            }
        }
    }

    // This function should probably be called as part of `updateUser` or `updateProfile`
    // It shouldn't be a standalone public function that triggers UI updates directly.
    // The image upload should be part of the overall profile update flow.
    // Let's integrate it into `updateProfile` below.
    /*
    fun uploadImage() {
        // ... (This logic will be moved to `updateProfile()`)
    }
    */

    // Unified update function
    fun updateProfile() { // Renamed from updateUser to be more comprehensive
        val currentUserId = currentUser?.uid
        if (currentUserId == null) {
            _updateViewState.update {
                it.copy(
                    errorMessage = "User not logged in.",
                    isLoading = false
                )
            }
            viewModelScope.launch { _eventFlow.emit(UpdateProfileEvent.ShowSnackbar("User not logged in.")) }
            return
        }

        viewModelScope.launch {
            _updateViewState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null,
                    successMessage = null
                )
            }

            var finalProfileImageUrl: String? =
                _updateViewState.value.profilePictureUrl // Start with existing URL

            // 1. Handle image upload if a new URI is selected
            if (_updateViewState.value.profilePictureUri != null) {
                _updateViewState.update { it.copy(isUploadingImage = true) }
                // Use a suspend function for uploadImage if possible, to make it sequential
                // If not, make sure the callback correctly updates the state.
                cloudinaryRepository.uploadImage(
                    _updateViewState.value.profilePictureUri!!,
                    "profile_images"
                ) { result ->
                    result.onSuccess { uploadedUrl ->
                        finalProfileImageUrl = uploadedUrl
                        _updateViewState.update { it.copy(isUploadingImage = false) }
                        // Continue with profile update after image upload success
                        performUserUpdate(currentUserId, finalProfileImageUrl)
                    }.onFailure { e ->
                        _updateViewState.update {
                            it.copy(
                                isUploadingImage = false,
                                isLoading = false,
                                errorMessage = e.localizedMessage ?: "Image upload failed."
                            )
                        }
                        viewModelScope.launch {
                            _eventFlow.emit(
                                UpdateProfileEvent.ShowSnackbar(
                                    e.localizedMessage ?: "Image upload failed."
                                )
                            )
                        }
                    }
                }
            } else {
                // No new image selected, proceed directly with user data update
                performUserUpdate(currentUserId, finalProfileImageUrl)
            }
        }
    }

    private fun performUserUpdate(uid: String, profileImageUrl: String?) {
        viewModelScope.launch {
            val updates = hashMapOf<String, Any>(
                "username" to _updateViewState.value.username,
                "fullName" to _updateViewState.value.fullName, // Ensure this matches your User model and Firestore field
                "bio" to _updateViewState.value.bio
            )
            profileImageUrl?.let { url -> // Only add profilePictureUrl if it's not null
                updates["profilePic"] =
                    url // Ensure this matches your Firestore field name (e.g., "profilePic" or "profilePictureUrl")
            }

            userRepository.updateUser(uid, updates)
                .onSuccess { successMessage -> // Assuming userRepository returns a success message or unit
                    _updateViewState.update {
                        it.copy(
                            isLoading = false,
                            successMessage = "Update Successful", // Consistent message
                            profilePictureUrl = profileImageUrl // Update the UI state with the new URL
                        )
                    }
                    _eventFlow.emit(
                        UpdateProfileEvent.ShowSnackbar(
                            successMessage ?: "Profile updated successfully!"
                        )
                    )
                    _eventFlow.emit(UpdateProfileEvent.UpdateSuccess) // Trigger navigation
                }
                .onFailure { e ->
                    _updateViewState.update {
                        it.copy(
                            errorMessage = e.localizedMessage ?: "Unknown error",
                            isLoading = false,
                        )
                    }
                    _eventFlow.emit(
                        UpdateProfileEvent.ShowSnackbar(
                            e.localizedMessage ?: "Failed to update profile."
                        )
                    )
                }
        }
    }


    // --- UI Event Handlers (These were already correct) ---
    fun onUsernameChanged(newUsername: String) {
        _updateViewState.update {
            it.copy(
                username = newUsername,
                successMessage = null,
                errorMessage = null
            )
        }
    }

    fun onNameChanged(newName: String) {
        _updateViewState.update {
            it.copy(
                fullName = newName,
                successMessage = null,
                errorMessage = null
            )
        }
    }

    fun onBioChanged(newBio: String) {
        _updateViewState.update {
            it.copy(
                bio = newBio,
                successMessage = null,
                errorMessage = null
            )
        }
    }

    fun onProfilePictureSelected(uri: Uri?) {
        _updateViewState.update {
            it.copy(
                profilePictureUri = uri,
                successMessage = null,
                errorMessage = null
            )
        }
    }
}

// Define your one-time events
sealed interface UpdateProfileEvent {
    data class ShowSnackbar(val message: String) : UpdateProfileEvent
    object UpdateSuccess : UpdateProfileEvent // To signal successful profile update for navigation
}

// Your data class for UI state (copied from your snippet, looks good)
data class UpdateProfileUiState(
    val username: String = "",
    val fullName: String = "", // Used `fullName` instead of `name` to match your `onNameChanged`
    val bio: String = "",
    val profilePictureUri: Uri? = null, // For local image selection
    val profilePictureUrl: String? = null, // For existing network image URL
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val isUploadingImage: Boolean = false // Specific state for image upload
)