package com.saswat10.instagramclone.data.mapper

import com.saswat10.instagramclone.data.model.PostDto
import com.saswat10.instagramclone.domain.models.Post
import com.saswat10.instagramclone.presentation.components.posts.Media
import com.saswat10.instagramclone.presentation.components.posts.MediaType
import java.time.ZoneId

object PostMapper {
    fun PostDto.toPost(): Post {
        return Post(
            id = this.id,
            username = this.username,
            userId = this.userId,
            profilePic = this.profilePic,
            caption = this.caption,
            media = this.media.map { it -> it.toMedia() },
            likesCount = this.likesCount,
            commentsCount = this.commentsCount,
            createdAt = this.createdAt?.toDate()?.toInstant()?.atZone(ZoneId.systemDefault())
                ?.toLocalDateTime()
        )
    }


    fun PostDto.MediaDto.toMedia(): Media {
        return Media(
            id = this.id.toIntOrNull() ?: 0,
            url = this.url,
            type = MediaType.fromType(this.type)?: MediaType.IMAGE
        )
    }
}