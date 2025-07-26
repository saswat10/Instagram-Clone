package com.saswat10.instagramclone.domain.repository

import com.saswat10.instagramclone.domain.models.Post

interface IPostRepository {

    suspend fun createPost(post: Post): Result<Unit>
}