package com.saswat10.instagramclone.presentation.components.user

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun PasswordValidation(password: String, confirmPassword: String) {
    Column(Modifier.fillMaxWidth()) {
        val password = password
        val confirmPassword = confirmPassword
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
}