package com.saswat10.instagramclone.screens.authScreens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.saswat10.instagramclone.components.common.SimpleHeader
import com.saswat10.instagramclone.viewmodels.RegisterViewModel
import com.saswat10.instagramclone.viewmodels.RegisterViewState
import timber.log.Timber

@Composable
fun RegisterScreen(
    registerViewModel: RegisterViewModel = hiltViewModel<RegisterViewModel>(),
    navController: NavHostController
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }
    val viewState by registerViewModel.viewState.collectAsState()
    Column(modifier = Modifier.fillMaxSize()) {
        SimpleHeader("Register", onBack = {navController.popBackStack()})

        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
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
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
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
                    onClick = {
                        registerViewModel.register(email, password, confirmPassword)
                    },
                    enabled = (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty() && password == confirmPassword)
                ) {
                    if (viewState == RegisterViewState.Loading) CircularProgressIndicator(
                        modifier = Modifier
                            .size(
                                20.dp
                            )
                            .fillMaxWidth(), color = MaterialTheme.colorScheme.onPrimary
                    )
                    else Text("Sign In")
                }
            }
            when (viewState) {
                is RegisterViewState.Loading -> {}
                is RegisterViewState.Error -> {}
                is RegisterViewState.Success -> {
                    Text("Success")
                    Timber.tag("Success").d((viewState as RegisterViewState.Success).user?.email)
                }

                else -> {}
            }


//                Box() {
//                    HorizontalDivider(modifier = Modifier.align(Alignment.Center))
//                    Text(
//                        "OR",
//                        modifier = Modifier
//                            .align(Alignment.Center)
//                            .background(color = MaterialTheme.colorScheme.surface)
//                            .padding(horizontal = 10.dp),
//                        color = Color.Gray
//                    )
//                }
//
//                TextButton(onClick = {
//                    Timber.tag("Forgot").d("")
//                }) {
//                    Image(
//                        painter = painterResource(R.drawable.google),
//                        contentDescription = null,
//                        Modifier.size(20.dp)
//                    )
//                    Spacer(Modifier.width(10.dp))
//                    Text("Sign Up With Google")
//                }

        }
    }

}
