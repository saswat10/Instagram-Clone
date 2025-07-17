package com.saswat10.instagramclone.models.remote

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

@IgnoreExtraProperties
data class RemoteFriends(
    @DocumentId val id: String = "",
    val userIds: List<String> = emptyList(),
    val user1: DenormalizedUser? = null,
    val user2: DenormalizedUser? = null,
    @ServerTimestamp val createdAt: Timestamp? = null
)
