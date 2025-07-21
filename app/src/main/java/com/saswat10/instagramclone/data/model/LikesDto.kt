package com.saswat10.instagramclone.data.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class LikesDto(
    @DocumentId val id : String = "",
    val userId : String = "",
    val username: String = "",
    val profilePic: String = ""
)