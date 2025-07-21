package com.saswat10.instagramclone.data.remote

import com.google.firebase.firestore.DocumentSnapshot
import com.saswat10.instagramclone.data.model.UserDto
import kotlinx.coroutines.flow.Flow

interface IUserService {

    suspend fun createNewUser(uid: String, remoteUser: UserDto): Result<Unit>

    suspend fun updateUser(uid: String, remoteUser: UserDto): Result<Unit>

    suspend fun getUsers(): Result<List<UserDto?>>

    suspend fun getUserByUid(uid: String): Result<UserDto?>

    fun observeUser(uid: String): Flow<Result<UserDto?>>

    fun getUsersPaginated(
        limit: Long = 10,
        lastDocumentSnapshot: DocumentSnapshot? = null
    ): Flow<Result<Pair<List<UserDto?>, DocumentSnapshot?>>>

    suspend fun deleteUser(uid: String): Result<Unit>
}
