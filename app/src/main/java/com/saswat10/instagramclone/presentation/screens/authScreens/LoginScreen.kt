package com.saswat10.instagramclone.presentation.screens.authScreens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.saswat10.instagramclone.navigation.AuthNavRoutes
import com.saswat10.instagramclone.viewmodels.LoginViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel<LoginViewModel>(),
    navigateTo: ((id: Any) -> Unit),
) {

    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {

            Text(
                text = "JETGRAM",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold

            )
            Spacer(Modifier)
            OutlinedTextField(
                value = uiState.email,
                onValueChange = viewModel::onEmailChanged,
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = uiState.password,
                onValueChange = viewModel::onPasswordChanged,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = viewModel::toggleShowPassword) {
                        Icon(
                            imageVector = if (uiState.showPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                            contentDescription = null
                        )
                    }
                },
                visualTransformation = if (uiState.showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                label = { Text("Password") }, modifier = Modifier.fillMaxWidth()
            )


            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = {
                    navigateTo(AuthNavRoutes.PasswordScreen)
                }) {
                    Text("Forgot Password?")
                }
                Spacer(Modifier.width(5.dp))
                Button(onClick = viewModel::onLoginClicked) {
                    if (uiState.loading) CircularProgressIndicator(
                        modifier = Modifier
                            .size(
                                20.dp
                            )
                            .fillMaxWidth(), color = MaterialTheme.colorScheme.onPrimary
                    )
                    else
                        Text("Sign In")
                }
            }

        }
        Column(
            Modifier
                .fillMaxWidth()
                .align(Alignment.BottomEnd)
        ) {
            HorizontalDivider()
            Row(
                modifier = Modifier
                    .padding(bottom = 30.dp, top = 15.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    "Don't have an account? ",
                )
                Text(
                    "Sign Up",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        navigateTo(AuthNavRoutes.RegisterScreen)
                    }
                )
            }

        }
    }
}
