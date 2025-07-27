package com.saswat10.instagramclone.presentation.screens.authScreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.saswat10.instagramclone.presentation.components.user.PasswordValidation
import com.saswat10.instagramclone.viewmodels.RegisterViewModel

@Composable
fun RegisterScreen(
    registerViewModel: RegisterViewModel = hiltViewModel<RegisterViewModel>(),
    navigateTo: ((id: Any) -> Unit),
    navigateToUpdate: ()-> Unit
) {
    val viewState by registerViewModel.viewState.collectAsState()

    LaunchedEffect(viewState.registrationSuccess) {
        if(viewState.registrationSuccess == true){
            navigateToUpdate()
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {

            Text(
                text = "JETGRAM",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold

            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = viewState.username,
                onValueChange = registerViewModel::onUsernameChange,
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = viewState.fullName,
                onValueChange = registerViewModel::onFullNameChange,
                label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = viewState.email,
                onValueChange = registerViewModel::onEmailChange,
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = viewState.password,
                onValueChange = registerViewModel::onPasswordChange,
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = registerViewModel::togglePasswordShow) {
                        Icon(
                            imageVector = if (viewState.showPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                            contentDescription = null
                        )
                    }
                },
                visualTransformation = if (viewState.showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            )
            OutlinedTextField(
                value = viewState.confirmPassword,
                onValueChange = registerViewModel::onConfirmPasswordChange,
                label = { Text("Confirm Password") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = registerViewModel::toggleConfirmPasswordShow) {
                        Icon(
                            imageVector = if (viewState.showConfirmPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                            contentDescription = null
                        )
                    }
                },
                visualTransformation = if (viewState.showConfirmPassword) VisualTransformation.None else PasswordVisualTransformation(),
            )
            PasswordValidation(viewState.password, viewState.confirmPassword)

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Spacer(Modifier.width(5.dp))
                Button(
                    onClick = registerViewModel::registerUser,

                    ) {
                    Text("Sign In")
                }
            }

        }
    }
}
