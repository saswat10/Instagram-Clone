package com.saswat10.instagramclone.screens.authScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.saswat10.instagramclone.R
import com.saswat10.instagramclone.viewmodels.LoginViewModel
import com.saswat10.instagramclone.viewmodels.LoginViewState
import timber.log.Timber

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel<LoginViewModel>(),
    navigateToRegister: () -> Unit,
    navigateToPassword: () -> Unit
) {

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var showPassword by rememberSaveable { mutableStateOf(false) }

    val state by viewModel.viewState.collectAsState()


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
                style = MaterialTheme.typography.headlineLarge + TextStyle(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.Cyan,
                            Color.Yellow,
                        )
                    )
                ),
                fontWeight = FontWeight.Bold

            )
            Spacer(Modifier)
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(
                            imageVector = if (showPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                            contentDescription = null
                        )
                    }
                },
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                label = { Text("Password") }, modifier = Modifier.fillMaxWidth()
            )


            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = {
                    navigateToPassword()
                }) {
                    Text("Forgot Password?")
                }
                Spacer(Modifier.width(5.dp))
                Button(onClick = {
                    viewModel.login(email, password)
                }) {
                    if (state == LoginViewState.Loading) CircularProgressIndicator(
                        modifier = Modifier.size(
                            20.dp
                        ).fillMaxWidth(), color = MaterialTheme.colorScheme.onPrimary
                    )
                    else
                        Text("Sign In")
                }
            }

            when (state) {
                is LoginViewState.Loading -> {
//                    CircularProgressIndicator()
                }
//                is LoginViewState.Error ->{
//                    Text((state as LoginViewState.Error).message)
//                }
                is LoginViewState.Success -> {
                    Text("Success")
                    Timber.tag("Success").d((state as LoginViewState.Success).user?.uid)
                }

                else -> {}
            }


//            Box() {
//                HorizontalDivider(modifier = Modifier.align(Alignment.Center))
//                Text(
//                    "OR",
//                    modifier = Modifier
//                        .align(Alignment.Center)
//                        .background(color = MaterialTheme.colorScheme.surface)
//                        .padding(horizontal = 10.dp),
//                    color = Color.Gray
//                )
//            }
//
//            TextButton(onClick = {
//                Timber.tag("Forgot").d("")
//            }) {
//                Image(
//                    painter = painterResource(R.drawable.google),
//                    contentDescription = null,
//                    Modifier.size(20.dp)
//                )
//                Spacer(Modifier.width(10.dp))
//                Text("Sign In With Google")
//            }
//
//
//            Spacer(modifier)
        }
        Column(
            Modifier
                .fillMaxWidth()
                .align(Alignment.BottomEnd)
        ) {
            HorizontalDivider()
            Row(
                modifier = Modifier
                    .padding(bottom = 30.dp, top = 15.dp )
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
                        navigateToRegister()
                    }
                )
            }

        }
    }
}
