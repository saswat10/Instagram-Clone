package com.saswat10.instagramclone.data.remote

import com.google.firebase.firestore.DocumentSnapshot
import com.saswat10.instagramclone.data.model.CommentDto
import com.saswat10.instagramclone.data.model.PostDto
import kotlinx.coroutines.flow.Flow

interface IPostService {

    suspend fun createPost(post: PostDto): Result<Unit>
    suspend fun updatePost(postId: String, post: PostDto): Result<Unit>
    suspend fun deletePost(postId: String): Result<Unit>
    suspend fun getPosts(userId: String): Result<List<PostDto?>>
    fun getPostsPaginated(
        limit: Long, lastDocumentSnapshot: DocumentSnapshot?
    ): Flow<Result<Pair<List<PostDto?>, DocumentSnapshot?>>>

    suspend fun getPostById(postId: String): Result<PostDto?>
    suspend fun isLiked(userId: String, postId: String): Result<Boolean>
    suspend fun toggleLike(userId: String, postId: String, userMap: HashMap<String, Any?>): Result<Unit>
    suspend fun createComment(postId: String, comment: CommentDto): Result<Unit>
    suspend fun updateComment(
        postId: String,
        commentId: String,
        comment: CommentDto
    ): Result<Unit>

    suspend fun deleteComment(postId: String, commentId: String): Result<Unit>
    fun getComments(
        postId: String,
        limit: Long,
        lastDocumentSnapshot: DocumentSnapshot?
    ): Flow<Result<Pair<List<CommentDto?>, DocumentSnapshot?>>>
}