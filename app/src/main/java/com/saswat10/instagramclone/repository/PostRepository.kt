package com.saswat10.instagramclone.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import com.saswat10.instagramclone.data.model.CommentDto
import com.saswat10.instagramclone.data.model.PostDto
import com.saswat10.instagramclone.utils.FirebaseConstantsV2
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PostRepository @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {

    private val postCollection = fireStore.collection(FirebaseConstantsV2.Posts.COLLECTION_POSTS)
    private val currentUser = auth.currentUser

    suspend fun createPost(post: PostDto): Result<String> {
        if (currentUser == null) {
            throw IllegalStateException("Current user is null")
        }
        return try {
            postCollection.add(post).await()
            Result.success("Post created successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updatePost(postId: String, post: PostDto): Result<String> {
        if (currentUser == null) {
            throw IllegalStateException("Current user is null")
        }
        return try {
            val postRef = postCollection.document(postId).get().await()
            if (postRef.exists()) {
                postRef.toObject(PostDto::class.java)?.let {

                }
                postCollection.document(postId).update(post.toMap()).await()
                Result.success("Post updated successfully")
            } else {
                Result.failure(Exception("Post with $postId not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deletePost(postId: String): Result<String> {
        if (currentUser == null) {
            throw IllegalStateException("Current user is null")
        }
        return try {
            val postRef = postCollection.document(postId).get().await()
            if (postRef.exists()) {
                val post = postRef.toObject(PostDto::class.java)
                if (post?.userId != currentUser.uid) {
                    Result.failure(Exception("You are not authorized to delete this post"))
                } else {
                    postCollection.document(postId).delete().await()
                    Result.success("Post deleted successfully")
                }
            } else {
                Result.failure(Exception("Post with $postId not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getPostsPaginated(
        limit: Long = 10, lastDocumentSnapshot: DocumentSnapshot? = null
    ): Flow<Result<Pair<List<PostDto?>, DocumentSnapshot?>>> {
        return flow {
            val currentUserId = currentUser?.uid

            if (currentUserId == null) {
                emit(Result.failure(Exception("Current user is null")))
                return@flow
            }

            var query = postCollection.orderBy(
                FirebaseConstantsV2.Common.CREATED_AT, Query.Direction.DESCENDING
            ).limit(limit)

            if (lastDocumentSnapshot != null) {
                query = query.startAfter(lastDocumentSnapshot)
            }

            query.snapshots().map { queryDocumentSnapshots ->
                val posts = queryDocumentSnapshots.documents.mapNotNull { documentSnapshot ->
                    documentSnapshot.toObject(PostDto::class.java)
                }
                val newLastDoc = queryDocumentSnapshots.documents.lastOrNull()
                Result.success(Pair(posts, newLastDoc))
            }.catch {
                emit(Result.failure(it))
            }.collect {
                emit(it)
            }
        }
    }

    suspend fun getSinglePost(postId: String): Result<PostDto?> {
        return try {
            val postRef = postCollection.document(postId).get().await()
            if (postRef.exists()) {
                val post = postRef.toObject(PostDto::class.java)
                Result.success(post)
            } else {
                Result.failure(Exception("Post with $postId not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun isLiked(postId: String): Result<Boolean> {
        if (currentUser == null) {
            return Result.failure(Exception("Current user is null"))
        }
        return try {
            val isLikedByUser = postCollection.document(postId)
                .collection(FirebaseConstantsV2.Posts.SUBCOLLECTION_LIKES)
                .document(currentUser.uid).get().await()
            Result.success(isLikedByUser.exists())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun toggleLike(postId: String, userMap: HashMap<String, Any?>): Result<String> {
        if (currentUser == null) {
            return Result.failure(Exception("Current user is null")) // Or a specific AuthException
        }
        val currentUserId = currentUser.uid // Get UID once

        return try {
            val result = fireStore.runTransaction { transaction ->
                val likeDocRef = postCollection.document(postId)
                    .collection(FirebaseConstantsV2.Posts.SUBCOLLECTION_LIKES)
                    .document(currentUserId)
                val postRef = postCollection.document(postId)

                val likeDoc = transaction.get(likeDocRef)

                if (likeDoc.exists()) {
                    // It's liked, so unlike it
                    transaction.delete(likeDocRef)
                    transaction.update(
                        postRef, // Use the postRef directly
                        FirebaseConstantsV2.Posts.FIELD_LIKES_COUNT,
                        FieldValue.increment(-1)
                    )
                    "Post unliked successfully"
                } else {
                    // It's not liked, so like it
                    transaction.set(likeDocRef, userMap)
                    transaction.update(
                        postRef, // Use the postRef directly
                        FirebaseConstantsV2.Posts.FIELD_LIKES_COUNT,
                        FieldValue.increment(1)
                    )
                    "Post liked successfully"
                }
            }.await()
            Result.success(result) // Wrap the success message
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createComment(postId: String, comment: CommentDto): Result<String> {
        if (currentUser == null) {
            Result.failure<Exception>(Exception("Current user is null"))
        }
        return try {
            val transactionResult = fireStore.runTransaction { transaction ->
                val postDocRef = postCollection.document(postId)
                val postSnapshot = transaction.get(postDocRef)

                if (postSnapshot.exists()) {
                    val newCommentDocRef =
                        postDocRef.collection(FirebaseConstantsV2.Posts.SUBCOLLECTION_COMMENTS)
                            .document()
                    transaction.set(newCommentDocRef, comment)

                    transaction.update(
                        postDocRef,
                        FirebaseConstantsV2.Posts.FIELD_COMMENTS_COUNT,
                        FieldValue.increment(1)
                    )
                    "Comment added successfully"
                } else {
                    throw IllegalArgumentException("Post with ID '$postId' does not exist.")
                }
            }.await()

            Result.success(transactionResult)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateComment(
        postId: String,
        commentId: String,
        comment: CommentDto
    ): Result<String> {
        if (currentUser == null) {
            Result.failure<Exception>(Exception("Current user is null"))
        }
        return try {
            val result = postCollection.document(postId)
                .collection(FirebaseConstantsV2.Posts.SUBCOLLECTION_COMMENTS).document(commentId)
                .update(comment.toMap()).await()
            Result.success("Comment updated successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteComment(postId: String, commentId: String): Result<String> {
        if (currentUser == null) {
            Result.failure<Exception>(Exception("Current user is null"))
        }
        return try {
            postCollection.document(postId)
                .collection(FirebaseConstantsV2.Posts.SUBCOLLECTION_COMMENTS)
                .document(commentId)
                .delete().await()
            Result.success("Comment deleted successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getComments(
        postId: String,
        limit: Long = 10,
        lastDocumentSnapshot: DocumentSnapshot? = null
    ): Flow<Result<Pair<List<CommentDto?>, DocumentSnapshot?>>> {
        return flow {
            val currentUserId = currentUser?.uid

            if (currentUserId == null) {
                emit(Result.failure(Exception("Current user is null")))
                return@flow
            }

            var query = postCollection.document(postId)
                .collection(FirebaseConstantsV2.Posts.SUBCOLLECTION_COMMENTS)
                .orderBy(FirebaseConstantsV2.Common.CREATED_AT, Query.Direction.DESCENDING)
                .limit(limit)

            if (lastDocumentSnapshot != null) {
                query = query.startAfter(lastDocumentSnapshot)
            }

            query.snapshots().map { queryDocumentSnapshots ->
                val posts = queryDocumentSnapshots.documents.mapNotNull { documentSnapshot ->
                    documentSnapshot.toObject(CommentDto::class.java)
                }
                val newLastDoc = queryDocumentSnapshots.documents.lastOrNull()
                Result.success(Pair(posts, newLastDoc))
            }.catch {
                emit(Result.failure(it))
            }.collect {
                emit(it)
            }
        }
    }
}
