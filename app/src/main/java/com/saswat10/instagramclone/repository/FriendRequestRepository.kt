package com.saswat10.instagramclone.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import com.saswat10.instagramclone.models.remote.RemoteFriends
import com.saswat10.instagramclone.models.remote.RemoteRequests
import com.saswat10.instagramclone.utils.Constants
import com.saswat10.instagramclone.utils.FirebaseConstantsV2
import com.saswat10.instagramclone.utils.RelationshipStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FriendRequestRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
) {

    private val friendsCollection =
        firestore.collection(FirebaseConstantsV2.Friends.COLLECTION_FRIENDS)
    private val requestsCollection =
        firestore.collection(FirebaseConstantsV2.Requests.COLLECTION_REQUESTS)
    private val userCollection = firestore.collection(FirebaseConstantsV2.Users.COLLECTION_USERS)
    private val currentUser = auth.currentUser

    /*
    1. Send Request
    2. Accept Request === add to friends collection
    3. Reject Request
    4. Cancel Request
    5. Delete Friend
    6. Get Friends - should be paginated
    7. Get Requests - break into two parts and paginate them
    8. Check whether a friend or not
    9. Already Pending Request
     */


    suspend fun sendRequest(request: RemoteRequests): Result<String> {
        return try {
            requestsCollection.add(request).await()
            Result.success("Request sent successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun acceptRequest(requestId: String): Result<String> {
        return try {
            val requestSnap = requestsCollection.document(requestId).get().await()
            if (!requestSnap.exists()) {
                return Result.failure(Exception("Request not found"))
            }

            val request = requestSnap.toObject(RemoteRequests::class.java) ?: return Result.failure(
                Exception("Malformed request")
            )

            val friends = RemoteFriends(
                user1 = request.fromUser,
                user2 = request.toUser,
                userIds = listOf(request.fromUid, request.toUid)
            )

            val friendDoc = friendsCollection.document()
            firestore.runTransaction { transaction ->
                transaction.set(friendDoc, friends)
                transaction.update(
                    requestsCollection.document(requestId),
                    FirebaseConstantsV2.Requests.FIELD_STATUS,
                    Constants.Status.ACCEPTED
                )
                transaction.update(
                    userCollection.document(request.fromUid),
                    FirebaseConstantsV2.Users.FIELD_FRIENDS,
                    FieldValue.increment(1)
                )
                transaction.update(
                    userCollection.document(request.toUid),
                    FirebaseConstantsV2.Users.FIELD_FRIENDS,
                    FieldValue.increment(1)
                )
            }.await()

            Result.success("Request accepted successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun rejectRequest(requestId: String): Result<String> {
        return try {
            val requestSnap = requestsCollection.document(requestId).get().await()
            if (!requestSnap.exists()) {
                return Result.failure(Exception("Request not found"))
            } else {
                requestsCollection.document(requestId).update(
                    FirebaseConstantsV2.Requests.FIELD_STATUS, Constants.Status.REJECTED
                ).await()
                Result.success("Request rejected successfully")
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun cancelRequest(requestId: String): Result<String> {
        return try {
            val requestSnap = requestsCollection.document(requestId).get().await()
            if (!requestSnap.exists()) {
                Result.failure(Exception("Request not found"))
            } else {
                requestsCollection.document(requestId).delete().await()
                Result.success("Request cancelled successfully")
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteFriend(friendId: String): Result<String> {
        return try {
            val friendSnap = friendsCollection.document(friendId).get().await()
            if (!friendSnap.exists()) {
                Result.failure(Exception("Friendship Id not found"))
            } else {
                val friend =
                    friendSnap.toObject(RemoteFriends::class.java) ?: return Result.failure(
                        Exception("Malformed request")
                    )
                firestore.runTransaction { transaction ->
                    transaction.delete(
                        friendsCollection.document(friendId)
                    )
                    transaction.update(
                        userCollection.document(friend.userIds[0]),
                        FirebaseConstantsV2.Users.FIELD_FRIENDS,
                        FieldValue.increment(-1)
                    )
                    transaction.update(
                        userCollection.document(friend.userIds[1]),
                        FirebaseConstantsV2.Users.FIELD_FRIENDS,
                        FieldValue.increment(-1)
                    )
                }.await()
                Result.success("Removed friend")
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getFriendsFlowPaginated(
        limit: Long = 10, lastDocumentSnapshot: DocumentSnapshot? = null
    ): Flow<Result<Pair<List<RemoteFriends>, DocumentSnapshot?>>> {
        if (currentUser != null) {
            return flow {
                var query = friendsCollection.whereArrayContains(
                    FirebaseConstantsV2.Friends.FIELD_USER_IDS, currentUser.uid
                ).orderBy(FirebaseConstantsV2.Friends.CREATED_AT, Query.Direction.DESCENDING)
                    .limit(limit)

                if (lastDocumentSnapshot != null) {
                    query = query.startAfter(lastDocumentSnapshot)
                }

                query.snapshots().map { snapshots ->
                    val friendsList = snapshots.documents.mapNotNull { docSnap ->
                        docSnap.toObject(RemoteFriends::class.java)
                    }
                    val newLastDoc = snapshots.documents.lastOrNull()
                    Result.success(Pair(friendsList, newLastDoc))
                }.catch {
                    emit(Result.failure(it))
                }.collect {
                    emit(it)
                }
            }
        } else {
            throw IllegalStateException("Current user is null")
        }
    }

    /**
     * @param type - can be SENT, RECEIVED
     * @see FirebaseConstantsV2.Requests for type
     */
    fun getRequestsFlowPaginated(
        type: String, limit: Long = 10, lastDocumentSnapshot: DocumentSnapshot? = null
    ): Flow<Result<Pair<List<RemoteRequests?>, DocumentSnapshot?>>> {

        if (currentUser != null) {
            return flow {
                var query = requestsCollection.whereEqualTo(type, currentUser.uid).whereEqualTo(
                    FirebaseConstantsV2.Requests.FIELD_STATUS, Constants.Status.PENDING
                ).orderBy(
                    FirebaseConstantsV2.Requests.FIELD_CREATED_AT, Query.Direction.DESCENDING
                ).limit(limit)

                if (lastDocumentSnapshot != null) {
                    query = query.startAfter(lastDocumentSnapshot)
                }

                query.snapshots().map { snapshots ->
                    val requests = snapshots.documents.mapNotNull { docSnap ->
                        docSnap.toObject(RemoteRequests::class.java)
                    }
                    val newLastDoc = snapshots.documents.lastOrNull()
                    Result.success(Pair(requests, newLastDoc))
                }.catch {
                    emit(Result.failure(it))
                }.collect {
                    emit(it)
                }
            }
        } else {
            throw IllegalStateException("Current user is null")
        }
    }


    fun checkFriend(targetId: String): Flow<Result<Boolean>> {
        if (currentUser == null) throw IllegalStateException("Current user is null")
        else {
            return flow {
                friendsCollection.whereArrayContains(
                    FirebaseConstantsV2.Friends.FIELD_USER_IDS, targetId
                ).whereArrayContains(FirebaseConstantsV2.Friends.FIELD_USER_IDS, currentUser.uid)
                    .limit(1)
                    .snapshots().map {
                        Result.success(it.documents.isNotEmpty())
                    }.catch {
                        emit(Result.failure(it))
                    }.collect {
                        emit(it)
                    }
            }
        }
    }

    fun checkPendingRequest(targetId: String): Flow<Result<Boolean>> {
        if (currentUser == null) throw IllegalStateException("Current user is null") else {
            return flow {
                requestsCollection.whereEqualTo(
                    FirebaseConstantsV2.Requests.FIELD_SENDER, targetId
                ).whereEqualTo(
                    FirebaseConstantsV2.Requests.FIELD_RECEIVER, currentUser.uid
                ).whereEqualTo(FirebaseConstantsV2.Requests.FIELD_STATUS, Constants.Status.PENDING)
                    .limit(1)
                    .snapshots().map {
                        Result.success(it.documents.isNotEmpty())
                    }.catch {
                        emit(Result.failure(it))
                    }.collect {
                        emit(it)
                    }
            }
        }
    }

    fun checkSentRequest(targetId: String): Flow<Result<Boolean>> {
        if (currentUser == null) throw IllegalStateException("Current user is null") else {
            return flow {
                requestsCollection.whereEqualTo(
                    FirebaseConstantsV2.Requests.FIELD_RECEIVER, targetId
                ).whereEqualTo(
                    FirebaseConstantsV2.Requests.FIELD_SENDER, currentUser.uid
                ).whereEqualTo(FirebaseConstantsV2.Requests.FIELD_STATUS, Constants.Status.PENDING)
                    .limit(1)
                    .snapshots().map {
                        Result.success(it.documents.isNotEmpty())
                    }.catch {
                        emit(Result.failure(it))
                    }.collect {
                        emit(it)
                    }
            }
        }
    }

    fun getRelationshipStatus(targetId: String): Flow<Result<RelationshipStatus>> {
        return combine(
            checkFriend(targetId), checkPendingRequest(targetId), checkSentRequest(targetId)
        ) { isFriend, isPending, isSent ->

            val error = isFriend.exceptionOrNull()
                ?: isPending.exceptionOrNull()
                ?: isSent.exceptionOrNull()

            if (error != null) {
                return@combine Result.failure(error)
            }

            val isFriendResult = isFriend.getOrThrow()
            val isPendingResult = isPending.getOrThrow()
            val isSentResult = isSent.getOrThrow()

            when {
                isFriendResult -> Result.success(RelationshipStatus.FRIENDS)
                isPendingResult -> Result.success(RelationshipStatus.NOT_FRIENDS_RECEIVED_REQUEST)
                isSentResult -> Result.success(RelationshipStatus.NOT_FRIENDS_SENT_REQUEST)
                else -> Result.success(RelationshipStatus.NOT_FRIENDS_NO_REQUEST)
            }
        }
    }
}
