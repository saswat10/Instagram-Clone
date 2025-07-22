package com.saswat10.instagramclone.screens.userScreens


import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.BackHandler // Import BackHandler
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api // Add this annotation if you use ExperimentalMaterial3Api features
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.saswat10.instagramclone.components.common.SimpleHeader
import com.saswat10.instagramclone.viewmodels.UpdateProfileEvent
import com.saswat10.instagramclone.viewmodels.UpdateViewModel
import kotlinx.coroutines.flow.collectLatest // For SharedFlow collection

@OptIn(ExperimentalMaterial3Api::class) // Or remove if not using experimental features
@Composable
fun UpdateProfileScreen(
    navigateTo: ((id: Any)->Unit),
    updateViewModel: UpdateViewModel = hiltViewModel(),
) {
    val updateViewState by updateViewModel.updateViewState.collectAsState()

    // Combined loading state for button and UI disable
    val isProcessing = updateViewState.isLoading || updateViewState.isUploadingImage

    // For picking image from gallery
    val getContent =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { result: Uri? ->
            updateViewModel.onProfilePictureSelected(result)
        }

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        SimpleHeader("Update Profile") // No back button for first-time users
        Column(
            Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Picture Box
            Box(modifier = Modifier) {
                val imageUrl = when {
                    updateViewState.profilePictureUri != null -> updateViewState.profilePictureUri // New local image
                    updateViewState.profilePictureUrl != null -> updateViewState.profilePictureUrl // Existing remote image
                    else -> null
                }

                if (imageUrl == null) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        modifier = Modifier
                            .size(160.dp)
                            .clip(shape = CircleShape),
                        contentDescription = "Profile Image",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                } else {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "Profile Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(160.dp)
                            .clip(shape = CircleShape)
                    )
                }
                FilledIconButton(
                    onClick = { getContent.launch("image/*") },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(5.dp)
                        .border(
                            width = 4.dp,
                            color = MaterialTheme.colorScheme.surface,
                            shape = CircleShape
                        )
                ) {
                    Icon(imageVector = Icons.Rounded.Edit, contentDescription = null)
                }
            }

            // TextFields
            OutlinedTextField(
                value = updateViewState.username,
                onValueChange = updateViewModel::onUsernameChanged,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Display Name") },
                enabled = !isProcessing // Disable input when processing
            )
            OutlinedTextField(
                value = updateViewState.fullName,
                onValueChange = updateViewModel::onNameChanged,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Full Name") },
                enabled = !isProcessing
            )
            OutlinedTextField(
                value = updateViewState.bio,
                onValueChange = updateViewModel::onBioChanged,
                minLines = 10,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Say something about yourself") },
                enabled = !isProcessing
            )

            // Error Message Display
            if (updateViewState.errorMessage != null) {
                Text(
                    text = updateViewState.errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = updateViewModel::updateProfile, // Use updateProfile()
                    modifier = Modifier.padding(5.dp),
                    enabled = !isProcessing // Disable button when any process is active
                ) {
                    if (isProcessing) { // Check combined processing state
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text(text = "Update")
                    }
                }
            }
        }
    }
}