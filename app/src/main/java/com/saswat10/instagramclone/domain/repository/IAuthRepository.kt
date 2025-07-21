package com.saswat10.instagramclone.domain.repository

import com.google.firebase.auth.FirebaseUser
import com.saswat10.instagramclone.domain.models.User
import kotlinx.coroutines.flow.Flow

interface IAuthRepository {
    suspend fun login(email: String, password: String): Result<User?>
    suspend fun register(email: String, password: String): Result<User?>
    fun logout()
    fun getCurrentUser(): Result<User?>
    fun isLoggedIn(): Boolean
    fun observeAuthState(): Flow<User?>
}