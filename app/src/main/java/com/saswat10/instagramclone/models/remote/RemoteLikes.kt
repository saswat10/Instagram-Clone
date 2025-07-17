package com.saswat10.instagramclone.models.remote

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class RemoteLikes(
    @DocumentId val id : String = "",
    val userId : String = "",
    val username: String = "",
    val profilePic: String = ""
)