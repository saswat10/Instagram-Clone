package com.saswat10.instagramclone.screens.authScreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.saswat10.instagramclone.components.common.SimpleHeader
import com.saswat10.instagramclone.viewmodels.ResetPasswordViewModel

@Composable
fun ResetPassword(
    viewModel: ResetPasswordViewModel = hiltViewModel<ResetPasswordViewModel>(),
    navigateBack: (()->Unit)
) {
    var email by remember { mutableStateOf("") }
    val state by viewModel.viewState.collectAsState()



    Column {
        SimpleHeader("Password Reset", onBack = {navigateBack()})
        Column(
            Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                Modifier.fillMaxWidth(),
                label = { Text("Email") })
            Button(onClick = {
                viewModel.resetPassword(email)
            }, Modifier.align(Alignment.End)) {
                Text("Send Reset Email")
            }
        }
    }

}