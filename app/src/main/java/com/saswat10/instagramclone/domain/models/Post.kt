package com.saswat10.instagramclone.domain.models

import com.saswat10.instagramclone.presentation.components.posts.Media
import java.time.LocalDateTime

data class Post(
    val id: String = "",
    val username: String = "",
    val userId: String = "",
    val profilePic: String = "",
    val caption: String = "",
    val media: List<Media> = emptyList(),
    val likesCount: Int = 0,
    val commentsCount: Int = 0,
    val createdAt: LocalDateTime? = null,
)