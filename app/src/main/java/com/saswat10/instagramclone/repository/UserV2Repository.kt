package com.saswat10.instagramclone.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.saswat10.instagramclone.models.remote.RemoteUserV2
import com.saswat10.instagramclone.utils.FirebaseConstantsV2
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserV2Repository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
) {

    private val userCollection = firestore.collection(FirebaseConstantsV2.Users.COLLECTION_USERS)
    private val currentUser = auth.currentUser

    suspend fun createUser(user: RemoteUserV2): Result<String> {
        if (currentUser == null) {
            throw IllegalStateException("Current user is null")
        } else {
            return try {
                userCollection.document(currentUser.uid).set(user).await()
                Result.success("User created successfully")
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun updateUser(hasMap: HashMap<String, Any>): Result<String> {
        if (currentUser == null) {
            throw IllegalStateException("Current user is null")
        } else {
            return try {
                userCollection.document(currentUser.uid).update(hasMap).await()
                Result.success("User updated successfully")
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun getAllUsers(): Result<List<RemoteUserV2?>> {
        if (currentUser == null) {
            throw IllegalStateException("Current user is null")
        } else {
            return try {
                val result = userCollection.get().await()
                val users = result.documents.map {
                    it.toObject(RemoteUserV2::class.java)
                }
                Result.success(users)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    }

    suspend fun getUserByUid(uid: String): Result<RemoteUserV2?> {
        return try {
            val result = userCollection.document(uid).get().await()
            if (result.exists()) {
                val remoteUser = result.toObject(RemoteUserV2::class.java)
                Result.success(remoteUser)
            } else {
                Result.success(null)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteUser() {}

// TODO - add search functionality

}