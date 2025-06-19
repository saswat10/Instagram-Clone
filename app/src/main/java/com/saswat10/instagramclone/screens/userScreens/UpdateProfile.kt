package com.saswat10.instagramclone.screens.userScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.saswat10.instagramclone.R
import com.saswat10.instagramclone.components.common.SimpleHeader
import com.saswat10.instagramclone.repository.FirebaseAuthRepository
import com.saswat10.instagramclone.viewmodels.UpdateViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateProfile(updateViewModel: UpdateViewModel = hiltViewModel<UpdateViewModel>()) {
    var displayName by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }

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
                Image(
                    painter = painterResource(R.drawable.img1),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(160.dp)
                        .clip(shape = CircleShape)
                )
                FilledIconButton(
                    onClick = {
                        TODO()
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
            TextButton(onClick = {
                updateViewModel.signOut()
            }) {
                Text(text = "SignOut")
            }

        }

    }
}