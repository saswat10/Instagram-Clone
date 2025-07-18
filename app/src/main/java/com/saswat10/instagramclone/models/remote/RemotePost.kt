package com.saswat10.instagramclone.models.remote

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

@IgnoreExtraProperties
data class RemotePost(
    @DocumentId val id: String = "",
    val username: String = "",
    val userId: String = "",
    val profilePic: String = "",
    val caption: String = "",
    val media: List<Media> = emptyList(),
    val likesCount: Int = 0,
    val commentsCount: Int = 0,
    @ServerTimestamp val createdAt: Timestamp? = null,
){
    data class Media(
        val url: String = "",
        val type: String = ""
    )

    @Exclude
    fun toMap(): Map<String, Any?>{
        return mapOf(
            "username" to username,
            "userId" to userId,
            "profilePic" to profilePic,
            "caption" to caption,
            "media" to media,
            "likesCount" to likesCount,
            "commentsCount" to commentsCount,
            "createdAt" to createdAt
        )
    }
}