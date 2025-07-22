package com.saswat10.instagramclone.presentation.screens.authScreens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.saswat10.instagramclone.viewmodels.RegisterViewModel

@Composable
fun RegisterScreen(
    registerViewModel: RegisterViewModel = hiltViewModel<RegisterViewModel>(),
    navigateTo: ((id: Any) -> Unit),
) {
    var showPassword by rememberSaveable { mutableStateOf(false) }
    var showConfirmPassword by rememberSaveable { mutableStateOf(false) }
    val viewState by registerViewModel.viewState.collectAsState()
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
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(
                            imageVector = if (showPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                            contentDescription = null
                        )
                    }
                },
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            )
            OutlinedTextField(
                value = viewState.confirmPassword,
                onValueChange = registerViewModel::onConfirmPasswordChange,
                label = { Text("Confirm Password") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = { showConfirmPassword = !showConfirmPassword }) {
                        Icon(
                            imageVector = if (showConfirmPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                            contentDescription = null
                        )
                    }
                },
                visualTransformation = if (showConfirmPassword) VisualTransformation.None else PasswordVisualTransformation(),
            )
            Column(Modifier.fillMaxWidth()) {
                val password = viewState.password
                val confirmPassword = viewState.confirmPassword
                AnimatedVisibility(password.isNotEmpty() && password.length < 8) {
                    Text(
                        "Password Length should be at least 8 characters",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                AnimatedVisibility(password.isNotEmpty() && !password.contains(Regex("[A-Z]"))) {
                    Text(
                        "Password should contain at least one uppercase character",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                AnimatedVisibility(password.isNotEmpty() && !password.contains(Regex("[a-z]"))) {
                    Text(
                        "Password should contain at least one lowercase character",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                AnimatedVisibility(password.isNotEmpty() && !password.contains(Regex("[0-9]"))) {
                    Text(
                        "Password should contain at least one digit",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                AnimatedVisibility(password.isNotEmpty() && !password.contains(Regex("[#@$?]"))) {
                    Text(
                        "Password should contain at least one of the symbols #, @, $, ?",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                AnimatedVisibility(password.isNotEmpty() && confirmPassword.isNotEmpty() && password != confirmPassword) {
                    Text(
                        "Password don't match",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }


            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Spacer(Modifier.width(5.dp))
                Button(
                    onClick = registerViewModel::registerAndCreate,

                ) {
                    Text("Sign In")
                }
            }

        }
    }
}
