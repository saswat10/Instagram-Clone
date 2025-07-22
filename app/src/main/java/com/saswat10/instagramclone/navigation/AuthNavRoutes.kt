package com.saswat10.instagramclone.navigation

import kotlinx.serialization.Serializable

sealed class AuthNavRoutes() {
    @Serializable data object LoginScreen: AuthNavRoutes()
    @Serializable data object RegisterScreen: AuthNavRoutes()
    @Serializable data object PasswordScreen: AuthNavRoutes()
}