package com.saswat10.instagramclone.models.remote

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

data class RemoteComment(
    @DocumentId val id: String = "",
    val userId: String = "",
    val username: String = "",
    val profilePic: String =  "",
    val content: String = "",
    @ServerTimestamp val createdAt: Timestamp? = null
)