package com.saswat10.instagramclone.screens.userScreens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.saswat10.instagramclone.components.common.SimpleHeader
import com.saswat10.instagramclone.navigation.MainScreen
import com.saswat10.instagramclone.viewmodels.UpdateViewModel
import com.saswat10.instagramclone.viewmodels.UpdateViewState
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateProfile(
    updateViewModel: UpdateViewModel = hiltViewModel<UpdateViewModel>(),
    onBack:(()->Unit)?=null,
    navigateToMainScreen:(()->Unit)
) {
    var displayName by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var selectedUri by remember { mutableStateOf<Uri?>(null) }

    val updateViewState by updateViewModel.updateViewState.collectAsState()

    val getContent =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { result ->
            result?.let {
                selectedUri = it
            }
        }
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        SimpleHeader("Update Profile")
        Column(
            Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(modifier = Modifier) {
                if (selectedUri == null) {
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
                        model = selectedUri,
                        contentDescription = "Profile Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(160.dp)
                            .clip(shape = CircleShape)
                    )
                }
                FilledIconButton(
                    onClick = {
                        getContent.launch("image/*")
                    },
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
            OutlinedTextField(
                value = displayName,
                onValueChange = { displayName = it },
                Modifier.fillMaxWidth(),
                label = { Text("Display Name") })
            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                Modifier.fillMaxWidth(),
                label = { Text("Full Name") })
            OutlinedTextField(
                value = bio,
                onValueChange = { bio = it },
                minLines = 10,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Say something about yourself") }
            )
            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = {
                        if (selectedUri != null) {
                            updateViewModel.uploadImage(selectedUri!!) { url ->
                                if (url != null) {
                                    val hashMap = hashMapOf<String, Any>(
                                        "displayName" to displayName,
                                        "fullName" to fullName,
                                        "bio" to bio,
                                        "profilePic" to url
                                    )
                                    updateViewModel.updateUser(hashMap)
                                } else {
                                    Timber.d("Failed to Upload Image")
                                }
                            }
                        } else {
                            val hashmap = hashMapOf<String, Any>(
                                "username" to displayName,
                                "fullName" to fullName,
                                "bio" to bio
                            )
                            updateViewModel.updateUser(hashmap)
                        }


                    },
                    modifier = Modifier.padding(5.dp),
                    enabled = (selectedUri != null && displayName.isNotBlank() && fullName.isNotBlank() && bio.isNotBlank())
                ) {
                    if (updateViewState == UpdateViewState.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(
                                    20.dp
                                )
                                .fillMaxWidth(), color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text(text = "Update")
                    }
                }
            }


            when (updateViewState) {
                is UpdateViewState.Error -> {}
                is UpdateViewState.Success -> {
                    navigateToMainScreen()
                }

                is UpdateViewState.Loading -> {
                }

                else -> {}
            }
        }

    }
}