package com.saswat10.instagramclone.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp

data class CommentDto(
    @DocumentId val id: String = "",
    val userId: String = "",
    val username: String = "",
    val profilePic: String =  "",
    val content: String = "",
    @ServerTimestamp val createdAt: Timestamp? = null
){
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "userId" to userId,
            "username" to username,
            "profilePic" to profilePic,
            "content" to content,
            "createdAt" to createdAt
        )
    }
}