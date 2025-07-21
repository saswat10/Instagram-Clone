package com.saswat10.instagramclone.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

@IgnoreExtraProperties
data class FriendsDto(
    @DocumentId val id: String = "",
    val userIds: List<String> = emptyList(),
    val user1: UserReferenceDto? = null,
    val user2: UserReferenceDto? = null,
    @ServerTimestamp val createdAt: Timestamp? = null
)
