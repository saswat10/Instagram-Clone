package com.saswat10.instagramclone.screens.authScreens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.saswat10.instagramclone.R
import timber.log.Timber

@Composable
fun RegisterScreen(modifier: Modifier) {
    Column(modifier) {
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var confirmPassword by remember { mutableStateOf("") }
        var showPassword by remember { mutableStateOf(false) }
        var showConfirmPassword by remember { mutableStateOf(false) }



        Box(modifier = Modifier.fillMaxSize()) {

            Column(
                modifier = Modifier
                    .padding(20.dp),
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
                        IconButton(onClick = { showPassword = !showPassword }) {
                            Icon(
                                imageVector = if (showPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                contentDescription = null
                            )
                        }
                    },
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
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


                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    Spacer(modifier.width(5.dp))
                    Button(onClick = {
                        Timber.tag("Register").d("Email: $email, Password: $password")
                    }) {
                        Text("Register")
                    }
                }
                Box() {
                    HorizontalDivider(modifier = Modifier.align(Alignment.Center))
                    Text(
                        "OR",
                        modifier = Modifier
                            .align(Alignment.Center)
                            .background(color = MaterialTheme.colorScheme.surface)
                            .padding(horizontal = 10.dp),
                        color = Color.Gray
                    )
                }

                TextButton(onClick = {
                    Timber.tag("Forgot").d("")
                }) {
                    Image(
                        painter = painterResource(R.drawable.google),
                        contentDescription = null,
                        Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(10.dp))
                    Text("Sign Up With Google")
                }

                Spacer(modifier)

            }
        }
    }
}


//fun RegisterScreen(modifier: Modifier, firebaseAuth: FirebaseAuth) {
//    Column(modifier) {
//
//        var user by remember { mutableStateOf(firebaseAuth.currentUser) }
//
//        Text(user?.email.toString())
//
//        Button(modifier = modifier, onClick = {
//            firebaseAuth.createUserWithEmailAndPassword("saswat101@outlook.com", "1")
//                .addOnSuccessListener {  }
//                .addOnFailureListener {
//                    Timber.tag("Register").e(it.localizedMessage)
//                }
//        }) {
//            Text(text = "Register")
//        }
//
//
//        Button(modifier = modifier, onClick = {
//            firebaseAuth.signInWithEmailAndPassword("saswat1091@outlook.com", "Saswat@1091")
//                .addOnSuccessListener {
//                    user = it.user
//                }.addOnFailureListener {
//                    Timber.tag("Register").e(it.localizedMessage)
//                }
//        }) {
//            Text(text = "Login")
//        }
//
//        Button(modifier = modifier, onClick = {
//            user?.sendEmailVerification()
//        }) {
//            Text(text = "Email Verification")
//        }
//
//        Button(modifier = modifier, onClick = {
//        }) {
//            Text(text = "Logout")
//        }
//
//        Button(modifier = modifier, onClick = {
//            firebaseAuth.signOut()
//        }) {
//            Text(text = "Logout")
//        }
//    }
//}