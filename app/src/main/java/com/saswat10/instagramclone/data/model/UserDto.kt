package com.saswat10.instagramclone.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

@IgnoreExtraProperties
data class UserDto(
    @DocumentId val id: String = "",
    val username: String = "",
    val name: String = "",
    val bio: String = "",
    val friends: Int = 0,
    val posts: Int = 0,
    val profilePic: String = "",
    @ServerTimestamp val createdAt: Timestamp? = null
){

    @Exclude
    fun toMap(): Map<String, Any?>{
        return mapOf(
            "username" to username,
            "id" to id,
            "name" to name,
            "bio" to bio,
            "posts" to posts,
            "friends" to friends,
            "profilePic" to profilePic,
            "createdAt" to createdAt
        )
    }
}