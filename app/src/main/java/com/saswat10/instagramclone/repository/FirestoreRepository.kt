package com.saswat10.instagramclone.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.saswat10.instagramclone.models.remote.RemoteUser
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FirestoreRepository @Inject constructor(private val firestore: FirebaseFirestore) {


    suspend fun updateUser(
        uid: String,
        hashMap: HashMap<String, Any>,
    ): Result<String> {
        return try {
            suspendCoroutine { cont ->
                firestore.collection("users").document(uid).update(hashMap)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            cont.resume(Result.success("User updated successfully"))
                        } else {
                            cont.resumeWithException(task.exception ?: Exception("Unknown error"))
                        }
                    }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createUser(user: RemoteUser, uid: String): Result<String> {
        return try {
            suspendCoroutine { cont ->
                firestore.collection("users").document(uid).set(user)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            cont.resume(Result.success("User created successfully"))
                        } else {
                            cont.resumeWithException(task.exception ?: Exception("Unknown error"))
                        }
                    }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}