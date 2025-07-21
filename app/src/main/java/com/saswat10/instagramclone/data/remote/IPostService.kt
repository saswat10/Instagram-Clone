package com.saswat10.instagramclone.data.remote

import com.google.firebase.firestore.DocumentSnapshot
import com.saswat10.instagramclone.models.remote.RemoteComment
import com.saswat10.instagramclone.models.remote.RemotePost
import kotlinx.coroutines.flow.Flow

interface IPostService {

    suspend fun createPost(post: RemotePost): Result<Unit>
    suspend fun updatePost(postId: String, post: RemotePost): Result<Unit>
    suspend fun deletePost(postId: String): Result<Unit>
    suspend fun getPosts(userId: String): Result<List<RemotePost?>>
    fun getPostsPaginated(
        limit: Long, lastDocumentSnapshot: DocumentSnapshot?
    ): Flow<Result<Pair<List<RemotePost?>, DocumentSnapshot?>>>

    suspend fun getPostById(postId: String): Result<RemotePost?>
    suspend fun isLiked(userId: String, postId: String): Result<Boolean>
    suspend fun toggleLike(userId: String, postId: String, userMap: HashMap<String, Any?>): Result<Unit>
    suspend fun createComment(postId: String, comment: RemoteComment): Result<Unit>
    suspend fun updateComment(
        postId: String,
        commentId: String,
        comment: RemoteComment
    ): Result<Unit>

    suspend fun deleteComment(postId: String, commentId: String): Result<Unit>
    fun getComments(
        postId: String,
        limit: Long,
        lastDocumentSnapshot: DocumentSnapshot?
    ): Flow<Result<Pair<List<RemoteComment?>, DocumentSnapshot?>>>
}