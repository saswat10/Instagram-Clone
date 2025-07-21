package com.saswat10.instagramclone.data.remote.firebase

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import com.saswat10.instagramclone.data.remote.IFriendRequestService
import com.saswat10.instagramclone.data.model.FriendsDto
import com.saswat10.instagramclone.data.model.RequestDto
import com.saswat10.instagramclone.utils.Constants
import com.saswat10.instagramclone.utils.FirebaseConstantsV2
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FriendRequestService @Inject constructor(
    private val firestore: FirebaseFirestore
) : IFriendRequestService {
    private val friendsCollection =
        firestore.collection(FirebaseConstantsV2.Friends.COLLECTION_FRIENDS)
    private val requestsCollection =
        firestore.collection(FirebaseConstantsV2.Requests.COLLECTION_REQUESTS)
    private val userCollection = firestore.collection(FirebaseConstantsV2.Users.COLLECTION_USERS)


    override suspend fun sendRequest(request: RequestDto): Result<Unit> {
        return try {
            requestsCollection.add(request).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun acceptRequest(requestId: String): Result<Unit> {
        return try {
            val requestSnap = requestsCollection.document(requestId).get().await()
            if (!requestSnap.exists()) {
                return Result.failure(Exception("Request not found"))
            }

            val request = requestSnap.toObject(RequestDto::class.java) ?: return Result.failure(
                Exception("Malformed request")
            )

            val friends = FriendsDto(
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

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun rejectRequest(requestId: String): Result<Unit> {
        return try {
            val requestSnap = requestsCollection.document(requestId).get().await()
            if (!requestSnap.exists()) {
                return Result.failure(Exception("Request not found"))
            } else {
                requestsCollection.document(requestId).update(
                    FirebaseConstantsV2.Requests.FIELD_STATUS, Constants.Status.REJECTED
                ).await()
                Result.success(Unit)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun cancelRequest(requestId: String): Result<Unit> {
        return try {
            val requestSnap = requestsCollection.document(requestId).get().await()
            if (!requestSnap.exists()) {
                Result.failure(Exception("Request not found"))
            } else {
                requestsCollection.document(requestId).delete().await()
                Result.success(Unit)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteFriend(friendId: String): Result<Unit> {
        return try {
            val friendSnap = friendsCollection.document(friendId).get().await()
            if (!friendSnap.exists()) {
                Result.failure(Exception("Friendship Id not found"))
            } else {
                val friend =
                    friendSnap.toObject(FriendsDto::class.java) ?: return Result.failure(
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
                Result.success(Unit)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getFriendsFlowPaginated(
        currentUserId: String,
        limit: Long,
        lastDocumentSnapshot: DocumentSnapshot?
    ): Flow<Result<Pair<List<FriendsDto>, DocumentSnapshot?>>> {
        return flow {
            var query = friendsCollection.whereArrayContains(
                FirebaseConstantsV2.Friends.FIELD_USER_IDS, currentUserId
            ).orderBy(FirebaseConstantsV2.Friends.CREATED_AT, Query.Direction.DESCENDING)
                .limit(limit)

            if (lastDocumentSnapshot != null) {
                query = query.startAfter(lastDocumentSnapshot)
            }

            query.snapshots().map { snapshots ->
                val friendsList = snapshots.documents.mapNotNull { docSnap ->
                    docSnap.toObject(FriendsDto::class.java)
                }
                val newLastDoc = snapshots.documents.lastOrNull()
                Result.success(Pair(friendsList, newLastDoc))
            }.catch {
                emit(Result.failure(it))
            }.collect {
                emit(it)
            }
        }
    }

    /**
     * @param type - can be SENT, RECEIVED
     * @see FirebaseConstantsV2.Requests for type
     */
    override fun getRequestsFlowPaginated(
        currentUserId: String,
        type: String,
        limit: Long,
        lastDocumentSnapshot: DocumentSnapshot?
    ): Flow<Result<Pair<List<RequestDto?>, DocumentSnapshot?>>> {
        return flow {
            var query = requestsCollection.whereEqualTo(type, currentUserId).whereEqualTo(
                FirebaseConstantsV2.Requests.FIELD_STATUS, Constants.Status.PENDING
            ).orderBy(
                FirebaseConstantsV2.Requests.FIELD_CREATED_AT, Query.Direction.DESCENDING
            ).limit(limit)

            if (lastDocumentSnapshot != null) {
                query = query.startAfter(lastDocumentSnapshot)
            }

            query.snapshots().map { snapshots ->
                val requests = snapshots.documents.mapNotNull { docSnap ->
                    docSnap.toObject(RequestDto::class.java)
                }
                val newLastDoc = snapshots.documents.lastOrNull()
                Result.success(Pair(requests, newLastDoc))
            }.catch {
                emit(Result.failure(it))
            }.collect {
                emit(it)
            }
        }
    }

    override fun checkFriend(currentUserId: String, targetId: String): Flow<Result<Boolean>> {
        return flow {
            friendsCollection.whereArrayContains(
                FirebaseConstantsV2.Friends.FIELD_USER_IDS, targetId
            ).whereArrayContains(FirebaseConstantsV2.Friends.FIELD_USER_IDS, currentUserId)
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

    override fun checkPendingRequest(
        currentUserId: String,
        targetId: String
    ): Flow<Result<Boolean>> {
        return flow {
            requestsCollection.whereEqualTo(
                FirebaseConstantsV2.Requests.FIELD_SENDER, targetId
            ).whereEqualTo(
                FirebaseConstantsV2.Requests.FIELD_RECEIVER, currentUserId
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

    override fun checkSentRequest(currentUserId: String, targetId: String): Flow<Result<Boolean>> {
        return flow {
            requestsCollection.whereEqualTo(
                FirebaseConstantsV2.Requests.FIELD_RECEIVER, targetId
            ).whereEqualTo(
                FirebaseConstantsV2.Requests.FIELD_SENDER, currentUserId
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