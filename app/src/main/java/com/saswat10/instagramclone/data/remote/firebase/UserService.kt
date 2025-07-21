package com.saswat10.instagramclone.data.remote.firebase

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import com.saswat10.instagramclone.data.remote.IUserService
import com.saswat10.instagramclone.data.model.UserDto
import com.saswat10.instagramclone.utils.FirebaseConstantsV2
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserService @Inject constructor(
    private val firestore: FirebaseFirestore
) : IUserService {
    private val userCollection = firestore.collection(FirebaseConstantsV2.Users.COLLECTION_USERS)

    override suspend fun createNewUser(uid: String, remoteUser: UserDto): Result<Unit> {
        return try {
            userCollection.document(uid).set(remoteUser).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateUser(uid: String, remoteUser: UserDto): Result<Unit> {
        return try {
            userCollection.document(uid).update(remoteUser.toMap()).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUsers(): Result<List<UserDto?>> {
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

    override suspend fun getUserByUid(uid: String): Result<UserDto?> {
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

    override fun getUsersPaginated(
        limit: Long,
        lastDocumentSnapshot: DocumentSnapshot?
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

    override suspend fun deleteUser(uid: String): Result<Unit> {
        return try {
            userCollection.document(uid).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}