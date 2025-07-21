package com.saswat10.instagramclone.data.remote

import com.google.firebase.firestore.DocumentSnapshot
import com.saswat10.instagramclone.models.remote.RemoteUserV2
import kotlinx.coroutines.flow.Flow

interface IUserService {

    suspend fun createNewUser(uid: String, remoteUser: RemoteUserV2): Result<Unit>

    suspend fun updateUser(uid: String, remoteUser: RemoteUserV2): Result<Unit>

    suspend fun getUsers(): Result<List<RemoteUserV2?>>

    suspend fun getUserByUid(uid: String): Result<RemoteUserV2?>

    fun getUsersPaginated(
        limit: Long = 10,
        lastDocumentSnapshot: DocumentSnapshot? = null
    ): Flow<Result<Pair<List<RemoteUserV2?>, DocumentSnapshot?>>>

    suspend fun deleteUser(uid: String): Result<Unit>
}
