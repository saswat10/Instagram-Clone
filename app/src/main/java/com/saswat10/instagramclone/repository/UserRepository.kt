package com.saswat10.instagramclone.repository

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.saswat10.instagramclone.models.domain.User
import com.saswat10.instagramclone.models.remote.RemoteUser
import com.saswat10.instagramclone.models.remote.toUser
import com.saswat10.instagramclone.utils.FirebaseConstants
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class UserRepository @Inject constructor(private val firestore: FirebaseFirestore) {

    private val userCollection = firestore.collection(FirebaseConstants.COLLECTION_USERS)


    /*
    * Create user
    * */
    suspend fun createUser(uid: String, user: RemoteUser): Result<String> {
        return try {
            Timber.d("$user")
            userCollection.document(uid).set(user).await()
            Result.success("User created successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /*
    * Update user
    * */
    suspend fun updateUser(uid: String, hasMap: HashMap<String, Any>): Result<String> {
        return try {
            userCollection.document(uid).update(hasMap).await()
            Result.success("User updated successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Get All Users
     */
    suspend fun getAllUsers(): Result<List<User?>> {
        return try {
            val result = userCollection.get().await()
            val users = result.documents.map {
                it.toObject(RemoteUser::class.java)?.toUser()
            }
            Result.success(users)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getAllUsersFlow(): Flow<Result<List<User?>>> = callbackFlow{
        val registration = userCollection
            .addSnapshotListener { snapshot, e ->
                if(e != null){
                    Timber.e(e, "Listener failed")
                    trySend(Result.failure(e))
                    return@addSnapshotListener
                }

                if(snapshot != null){
                    val users = snapshot.documents.mapNotNull { documentSnapshot ->
                        documentSnapshot.toObject(RemoteUser::class.java)?.toUser()
                    }
                    trySend(Result.success(users))
                }else{
                    trySend(Result.success(emptyList()))
                }
            }

        awaitClose {
            registration.remove()
            Timber.d("Firestore snapshot listener removed for all users")
        }
    }


    /*
    * Get User by uid
    * */
    suspend fun getUserByUid(uid: String): Result<User?> {
        return try {
            val result = userCollection.document(uid).get().await()
            if (result.exists()) {
                val remoteUser = result.toObject(RemoteUser::class.java)
                val user = remoteUser?.toUser()
                Result.success(user)
            } else {
                Result.success(null)
            }
        } catch (e: Exception) {
            Result.failure(e)

        }
    }


    /*
    * Get Followers
    * */
    suspend fun getFollowers(uid: String): Result<List<User.UserPreview>> {
        return try {
            val documents =
                userCollection.document(uid).collection(FirebaseConstants.SUBCOLLECTIONS_FOLLOWERS)
                    .get().await()
            val followers = documents.toObjects(User.UserPreview::class.java)
            Result.success(followers)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /*
    * Get FollowingList
    * */
    suspend fun getFollowing(uid: String): Result<List<User.UserPreview>> {
        return try {
            val documents =
                userCollection.document(uid).collection(FirebaseConstants.SUBCOLLECTIONS_FOLLOWING)
                    .get().await()
            val following = documents.toObjects(User.UserPreview::class.java)
            Result.success(following)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /*
    * Get PendingAccepts
    * */
    suspend fun getPendingRequests(uid: String): Result<List<User.UserPreview>> {
        return try {
            val documents =
                userCollection.document(uid)
                    .collection(FirebaseConstants.SUBCOLLECTIONS_PENDING_ACCEPTS).get()
                    .await()
            val pendingAccepts = documents.toObjects(User.UserPreview::class.java)
            Result.success(pendingAccepts)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
    * Get SentRequests
    * */
    suspend fun getSentRequests(uid: String): Result<List<User.UserPreview>> {
        return try {
            val documents =
                userCollection.document(uid)
                    .collection(FirebaseConstants.SUBCOLLECTIONS_SENT_REQUESTS).get()
                    .await()
            val sentRequests = documents.toObjects(User.UserPreview::class.java)
            Result.success(sentRequests)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Follow User
     */
    suspend fun followUser(uid: String, targetUid: String): Result<Unit> {
        return try {
            firestore.runTransaction { transaction ->
                val currentUserRef = userCollection.document(uid)
                val targetUserRef = userCollection.document(targetUid)

                val targetUserDoc = transaction.get(targetUserRef)
                val targetUser = targetUserDoc.toObject(RemoteUser::class.java)
                    ?: throw Exception("User not found")

                val currentUserDoc = transaction.get(currentUserRef)
                val currentUser = currentUserDoc.toObject(RemoteUser::class.java)
                    ?: throw Exception("User not found")

                if (targetUser.private) {
                    val sentRequestData = User.UserPreview(
                        targetUid,
                        targetUser.username,
                        targetUser.fullName,
                        targetUser.profilePic
                    )
                    val pendingRequestData = User.UserPreview(
                        uid,
                        currentUser.username,
                        currentUser.fullName,
                        currentUser.profilePic
                    )

                    transaction.set(
                        currentUserRef.collection(FirebaseConstants.SUBCOLLECTIONS_SENT_REQUESTS)
                            .document(targetUid),
                        sentRequestData
                    )
                    transaction.set(
                        targetUserRef.collection(FirebaseConstants.SUBCOLLECTIONS_PENDING_ACCEPTS)
                            .document(uid),
                        pendingRequestData
                    )
                } else {
                    // Public Account
                    val followerData = User.UserPreview(
                        uid,
                        currentUser.username,
                        currentUser.fullName,
                        currentUser.profilePic
                    )
                    val followingData = User.UserPreview(
                        targetUid,
                        targetUser.username,
                        targetUser.fullName,
                        targetUser.profilePic
                    )

                    transaction.set(
                        currentUserRef.collection(FirebaseConstants.SUBCOLLECTIONS_FOLLOWING)
                            .document(targetUid),
                        followingData
                    )
                    transaction.set(
                        targetUserRef.collection(FirebaseConstants.SUBCOLLECTIONS_FOLLOWERS)
                            .document(uid),
                        followerData
                    )

                    // update the follower and following count
                    transaction.update(
                        currentUserRef,
                        FirebaseConstants.FIELD_FOLLOWING,
                        currentUser.followingCount + 1
                    )
                    transaction.update(
                        targetUserRef,
                        FirebaseConstants.FIELD_FOLLOWERS,
                        targetUser.followerCount + 1
                    )
                }
                Result.success(Unit)
            }.await()
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Unfollow User
     */
    suspend fun unfollowUser(uid: String, targetUid: String): Result<Unit> {
        return try {
            firestore.runTransaction { transaction ->
                val currentUserRef = userCollection.document(uid)
                val targetUserRef = userCollection.document(targetUid)

                // remove following list of current User
                transaction.delete(
                    currentUserRef.collection(FirebaseConstants.SUBCOLLECTIONS_FOLLOWING)
                        .document(targetUid)
                )
                transaction.update(
                    currentUserRef,
                    FirebaseConstants.FIELD_FOLLOWING,
                    FieldValue.increment(-1)
                )

                // remove from followers list of target User
                transaction.delete(
                    targetUserRef.collection(FirebaseConstants.SUBCOLLECTIONS_FOLLOWERS)
                        .document(uid)
                )
                transaction.update(
                    targetUserRef,
                    FirebaseConstants.FIELD_FOLLOWERS,
                    FieldValue.increment(-1)
                )
            }.await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Accept Request
     */
    suspend fun acceptRequest(uid: String, targetUid: String): Result<Unit> {
        return try {
            firestore.runTransaction { transaction ->
                val currentUserRef = userCollection.document(uid)
                val targetUserRef = userCollection.document(targetUid)

                val targetUserDoc = transaction.get(targetUserRef)
                val targetUser = targetUserDoc.toObject(RemoteUser::class.java)
                    ?: throw Exception("User not found")

                val currentUserDoc = transaction.get(currentUserRef)
                val currentUser = currentUserDoc.toObject(RemoteUser::class.java)
                    ?: throw Exception("User not found")

                val followerData = User.UserPreview(
                    targetUid,
                    targetUser.username,
                    targetUser.fullName,
                    targetUser.profilePic
                )
                val followingData = User.UserPreview(
                    uid,
                    currentUser.username,
                    currentUser.fullName,
                    currentUser.profilePic
                )


                // delete the sent requests from the target user
                transaction.delete(
                    targetUserRef.collection(FirebaseConstants.SUBCOLLECTIONS_SENT_REQUESTS)
                        .document(uid)
                )
                // add the current user to the following list of the target user
                transaction.set(
                    targetUserRef.collection(FirebaseConstants.SUBCOLLECTIONS_FOLLOWING)
                        .document(uid), followingData
                )
                // increment the following count for the target user
                transaction.update(
                    targetUserRef,
                    FirebaseConstants.FIELD_FOLLOWING,
                    FieldValue.increment(1)
                )

                // delete the pending accepts from the current user
                transaction.delete(
                    currentUserRef.collection(FirebaseConstants.SUBCOLLECTIONS_PENDING_ACCEPTS)
                        .document(uid)
                )
                // add the target user to the followers list of the current user
                transaction.set(
                    currentUserRef.collection(FirebaseConstants.SUBCOLLECTIONS_FOLLOWERS)
                        .document(uid), followerData
                )
                // increment the followers count of the current user
                transaction.update(
                    currentUserRef,
                    FirebaseConstants.FIELD_FOLLOWERS,
                    FieldValue.increment(1)
                )
            }.await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Decline Request
     */
    suspend fun declineRequest(uid: String, targetUid: String): Result<Unit> {
        return try {
            firestore.runTransaction { transaction ->
                val currentUserRef = userCollection.document(uid)
                val targetUserRef = userCollection.document(targetUid)

                // delete the pending accepts from the current user
                transaction.delete(
                    currentUserRef.collection(FirebaseConstants.SUBCOLLECTIONS_PENDING_ACCEPTS)
                        .document(targetUid)
                )

                // delete the sent requests from the target user
                transaction.delete(
                    targetUserRef.collection(FirebaseConstants.SUBCOLLECTIONS_SENT_REQUESTS)
                        .document(uid)
                )
            }.await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Withdraw Follow Request
     */
    suspend fun withdrawFollowRequest(uid: String, targetUid: String): Result<Unit> {
        return try {
            firestore.runTransaction { transaction ->
                val currentUserRef = userCollection.document(uid)
                val targetUserRef = userCollection.document(targetUid)

                // delete the sent requests from the current user
                transaction.delete(
                    currentUserRef.collection(FirebaseConstants.SUBCOLLECTIONS_SENT_REQUESTS)
                        .document(targetUid)
                )

                // delete the pending accepts from the target user
                transaction.delete(
                    targetUserRef.collection(FirebaseConstants.SUBCOLLECTIONS_PENDING_ACCEPTS)
                        .document(uid)
                )
            }.await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Delete User - Should Also Delete the Posts, Comments, Remove from followers, following list, remove from everywhere.git
     */
}
