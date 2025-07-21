package com.saswat10.instagramclone.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

@IgnoreExtraProperties
data class RequestDto(
    @DocumentId val id: String = "",
    val fromUid: String = "",
    val toUid: String = "",
    val status: String = "",
    val fromUser: DenormalizedUser? = null,
    val toUser: DenormalizedUser? = null,
    @ServerTimestamp val createdAt: Timestamp? = null
)

