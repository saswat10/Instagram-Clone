package com.saswat10.instagramclone.models.remote

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

@IgnoreExtraProperties
data class RemoteUserV2(
    @DocumentId val id: String = "",
    val username: String = "",
    val name: String = "",
    val bio: String = "",
    val friends: Int = 0,
    val posts: Int = 0,
    val profilePic: String = "",
    @ServerTimestamp val createdAt: Timestamp? = null
)