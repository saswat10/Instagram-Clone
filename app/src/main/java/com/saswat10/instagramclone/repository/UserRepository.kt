package com.saswat10.instagramclone.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.saswat10.instagramclone.models.domain.User
import com.saswat10.instagramclone.models.remote.RemoteUser
import com.saswat10.instagramclone.models.remote.toUser
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepository @Inject constructor(private val firestore: FirebaseFirestore) {

    /*
    * Create user
    * */
    suspend fun createUser(uid: String, user: RemoteUser): Result<String> {
        return try {
            firestore.collection("users").document(uid).set(user).await()
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
            firestore.collection("users").document(uid).update(hasMap).await()
            Result.success("User updated successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    /*
    * Get User by uid
    * */
    suspend fun getUserByUid(uid: String): Result<User?> {
        return try {
            val result = firestore.collection("users").document(uid).get().await()
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
                firestore.collection("users").document(uid).collection("followers").get().await()
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
                firestore.collection("users").document(uid).collection("following").get().await()
            val following = documents.toObjects(User.UserPreview::class.java)
            Result.success(following)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /*
    * Get PendingRequests
    * */
    suspend fun getPendingRequests(uid: String): Result<List<User.UserPreview>> {
        return try {
            val documents =
                firestore.collection("users").document(uid).collection("pending_requests").get()
                    .await()
            val pendingRequests = documents.toObjects(User.UserPreview::class.java)
            Result.success(pendingRequests)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /*
    * Get SentRequests
    * */
    suspend fun getSentRequests(uid: String): Result<List<User.UserPreview>> {
        return try {
            val documents =
                firestore.collection("users").document(uid).collection("sent_requests").get()
                    .await()
            val sentRequests = documents.toObjects(User.UserPreview::class.java)
            Result.success(sentRequests)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}