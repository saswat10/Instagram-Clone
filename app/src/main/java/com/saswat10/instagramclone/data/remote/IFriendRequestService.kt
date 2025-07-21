package com.saswat10.instagramclone.data.remote

import com.google.firebase.firestore.DocumentSnapshot
import com.saswat10.instagramclone.models.remote.RemoteFriends
import com.saswat10.instagramclone.models.remote.RemoteRequests
import kotlinx.coroutines.flow.Flow

interface IFriendRequestService {

    suspend fun sendRequest(request: RemoteRequests): Result<Unit>
    suspend fun acceptRequest(requestId: String): Result<Unit>
    suspend fun rejectRequest(requestId: String): Result<Unit>
    suspend fun cancelRequest(requestId: String): Result<Unit>
    suspend fun deleteFriend(friendId: String): Result<Unit>
    fun getFriendsFlowPaginated(
        currentUserId: String,limit: Long = 10, lastDocumentSnapshot: DocumentSnapshot? = null
    ): Flow<Result<Pair<List<RemoteFriends>, DocumentSnapshot?>>>
    fun getRequestsFlowPaginated(
        currentUserId: String,type: String, limit: Long = 10, lastDocumentSnapshot: DocumentSnapshot? = null
    ): Flow<Result<Pair<List<RemoteRequests?>, DocumentSnapshot?>>>
    fun checkFriend(currentUserId: String,targetId: String): Flow<Result<Boolean>>
    fun checkPendingRequest(currentUserId: String,targetId: String): Flow<Result<Boolean>>
    fun checkSentRequest(currentUserId: String,targetId: String): Flow<Result<Boolean>>
}