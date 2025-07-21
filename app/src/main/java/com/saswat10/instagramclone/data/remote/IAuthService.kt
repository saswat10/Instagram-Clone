package com.saswat10.instagramclone.data.remote

import com.google.firebase.auth.FirebaseUser

interface IAuthService {
    suspend fun login(email: String, password: String): FirebaseUser?
    suspend fun register(email: String, password: String): FirebaseUser?
    fun logout()
    fun getCurrentUser(): FirebaseUser?
}