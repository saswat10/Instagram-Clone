package com.saswat10.instagramclone.data.remote.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.saswat10.instagramclone.data.remote.IAuthService
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthService @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : IAuthService {
    override suspend fun login(
        email: String,
        password: String
    ): FirebaseUser?{
        val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
        return result.user
    }

    override suspend fun register(
        email: String,
        password: String
    ): FirebaseUser? {
        val result = firebaseAuth.createUserWithEmailAndPassword(
            email,
            password
        ).await()
        return result.user
    }

    override fun logout() {
        firebaseAuth.signOut()
    }

    override fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    override fun observeCurrentUser(): Flow<FirebaseUser?> {
        return callbackFlow {
           val authListener = FirebaseAuth.AuthStateListener{
               trySend(it.currentUser)
           }

            firebaseAuth.addAuthStateListener(authListener)
            awaitClose {
                firebaseAuth.removeAuthStateListener(authListener)
            }
        }
    }
}