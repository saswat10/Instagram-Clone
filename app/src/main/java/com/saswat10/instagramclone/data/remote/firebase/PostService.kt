package com.saswat10.instagramclone.data.remote.firebase

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import com.saswat10.instagramclone.data.model.CommentDto
import com.saswat10.instagramclone.data.model.PostDto
import com.saswat10.instagramclone.data.remote.IPostService
import com.saswat10.instagramclone.utils.FirebaseConstantsV2
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PostService @Inject constructor(
    private val firestore: FirebaseFirestore
) : IPostService {
    private val postCollection = firestore.collection(FirebaseConstantsV2.Posts.COLLECTION_POSTS)
    private val userCollection = firestore.collection(FirebaseConstantsV2.Users.COLLECTION_USERS)

    override suspend fun createPost(post: PostDto): Result<Unit> {
        return runCatching {
            firestore.runTransaction {
                val userRef = userCollection.document(post.userId)
                val newPostRef = postCollection.document()

                it.set(newPostRef, post)
                it.update(userRef, FirebaseConstantsV2.Users.FIELD_POSTS, FieldValue.increment(1))

            }.await()
        }
    }

    override suspend fun updatePost(
        postId: String,
        post: PostDto
    ): Result<Unit> {
        return try {
            val postRef = postCollection.document(postId).get().await()
            if (postRef.exists()) {
                postRef.toObject(PostDto::class.java)?.let {

                }
                postCollection.document(postId).update(post.toMap()).await()
                Result.success(Unit)
            } else {
                Result.failure(Exception("Post with $postId not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deletePost(postId: String): Result<Unit> {
        return try {
            val postRef = postCollection.document(postId).get().await()
            if (postRef.exists()) {
                val post = postRef.toObject(PostDto::class.java)
                postCollection.document(postId).delete().await()
                Result.success(Unit)
            } else {
                Result.failure(Exception("Post with $postId not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getPosts(userId: String): Result<List<PostDto?>> {
        return runCatching {
            val query = postCollection
                .whereEqualTo(FirebaseConstantsV2.Posts.FIELD_USER_ID, userId)
                .orderBy(FirebaseConstantsV2.Common.CREATED_AT, Query.Direction.DESCENDING)
                .get()
                .await()


            query.documents.mapNotNull {documentSnapshot ->
                documentSnapshot.toObject(PostDto::class.java)
            }
        }
    }

    override fun getPostsPaginated(
        limit: Long,
        lastDocumentSnapshot: DocumentSnapshot?
    ): Flow<Result<Pair<List<PostDto?>, DocumentSnapshot?>>> {
        return flow {
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

    override suspend fun getPostById(postId: String): Result<PostDto?> {
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

    override suspend fun isLiked(userId: String, postId: String): Result<Boolean> {
        return try {
            val isLikedByUser = postCollection.document(postId)
                .collection(FirebaseConstantsV2.Posts.SUBCOLLECTION_LIKES)
                .document(userId).get().await()
            Result.success(isLikedByUser.exists())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun toggleLike(
        userId: String,
        postId: String,
        userMap: HashMap<String, Any?>
    ): Result<Unit> {
        return try {
            firestore.runTransaction { transaction ->
                val likeDocRef = postCollection.document(postId)
                    .collection(FirebaseConstantsV2.Posts.SUBCOLLECTION_LIKES)
                    .document(userId)
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

                } else {
                    // It's not liked, so like it
                    transaction.set(likeDocRef, userMap)
                    transaction.update(
                        postRef, // Use the postRef directly
                        FirebaseConstantsV2.Posts.FIELD_LIKES_COUNT,
                        FieldValue.increment(1)
                    )
                }
            }.await()
            Result.success(Unit) // Wrap the success message
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createComment(
        postId: String,
        comment: CommentDto
    ): Result<Unit> {
        return try {
            firestore.runTransaction { transaction ->
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
                } else {
                    throw IllegalArgumentException("Post with ID '$postId' does not exist.")
                }
            }.await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateComment(
        postId: String,
        commentId: String,
        comment: CommentDto
    ): Result<Unit> {
        return try {
            val result = postCollection.document(postId)
                .collection(FirebaseConstantsV2.Posts.SUBCOLLECTION_COMMENTS).document(commentId)
                .update(comment.toMap()).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteComment(
        postId: String,
        commentId: String
    ): Result<Unit> {
        return try {
            postCollection.document(postId)
                .collection(FirebaseConstantsV2.Posts.SUBCOLLECTION_COMMENTS)
                .document(commentId)
                .delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getComments(
        postId: String,
        limit: Long,
        lastDocumentSnapshot: DocumentSnapshot?
    ): Flow<Result<Pair<List<CommentDto?>, DocumentSnapshot?>>> {
        return flow {
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