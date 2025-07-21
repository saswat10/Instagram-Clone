package com.saswat10.instagramclone.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import com.saswat10.instagramclone.data.model.UserDto
import com.saswat10.instagramclone.utils.FirebaseConstantsV2
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserV2Repository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
) {

    private val userCollection = firestore.collection(FirebaseConstantsV2.Users.COLLECTION_USERS)
    private val currentUser = auth.currentUser

    suspend fun createUser(user: UserDto): Result<String> {
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

    suspend fun getAllUsers(): Result<List<UserDto?>> {
        if (currentUser == null) {
            throw IllegalStateException("Current user is null")
        } else {
            return try {
                val result = userCollection.get().await()
                val users = result.documents.map {
                    it.toObject(UserDto::class.java)
                }
                Result.success(users)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    }

    suspend fun getUserByUid(uid: String): Result<UserDto?> {
        return try {
            val result = userCollection.document(uid).get().await()
            if (result.exists()) {
                val remoteUser = result.toObject(UserDto::class.java)
                Result.success(remoteUser)
            } else {
                Result.success(null)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getUsersPaginated(
        limit: Long = 10,
        lastDocumentSnapshot: DocumentSnapshot? = null
    ): Flow<Result<Pair<List<UserDto?>, DocumentSnapshot?>>> {
        return flow {
            var query = userCollection
                .orderBy(FirebaseConstantsV2.Common.CREATED_AT, Query.Direction.DESCENDING)
                .limit(limit)

            if (lastDocumentSnapshot != null) {
                query = query.startAfter(lastDocumentSnapshot)
            }

            query.snapshots().map { snapshots ->
                val users = snapshots.documents.mapNotNull { docSnap ->
                    docSnap.toObject(UserDto::class.java)
                }
                val newLastDoc = snapshots.documents.lastOrNull()
                Result.success(Pair(users, newLastDoc))
            }.catch {
                emit(Result.failure(it))
            }.collect {
                emit(it)
            }

        }
    }

    suspend fun deleteUser() {}

// TODO - add search functionality

}