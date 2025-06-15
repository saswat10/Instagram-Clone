package com.saswat10.instagramclone.repository

import com.google.firebase.FirebaseError
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.saswat10.instagramclone.models.User
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FirebaseAuthRepository @Inject constructor(private val auth: FirebaseAuth) {

    var currentUser: FirebaseUser = auth.currentUser!!

    suspend fun signIn(email: String, password: String): Result<FirebaseUser> {
        return try {
            currentUser = suspendCoroutine<FirebaseUser> { cont ->
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        cont.resume(auth.currentUser!!)
                    } else {
                        cont.resumeWithException(task.exception ?: Exception("Unknown error"))
                    }
                }
            }
            Result.success(currentUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun register(email: String, password: String): Result<FirebaseUser> {
        return try {
            currentUser = suspendCoroutine<FirebaseUser> { cont ->
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        cont.resume(auth.currentUser!!)
                    } else {
                        cont.resumeWithException(task.exception ?: Exception("Unknown error"))
                    }
                }
            }
            Result.success(currentUser)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun resetPassword(email: String): Result<String> {
        return try {
            suspendCoroutine { cont ->
                auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        cont.resume(Unit)
                    } else {
                        cont.resumeWithException(task.exception ?: Exception("Unknown error"))
                    }
                }
            }
            Result.success("Password reset email sent")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}