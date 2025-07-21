package com.saswat10.instagramclone.domain.models

import com.google.firebase.Timestamp
import com.google.type.DateTime
import java.time.LocalDateTime
import java.util.Date

data class User(
    val userId: String = "",
    val email: String? = "",
    val username: String ="",
    val name: String = "",
    val bio: String = "",
    val profilePic: String? = null,
    val posts: Int = 0,
    val friends: Int = 0,
)