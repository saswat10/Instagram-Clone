package com.saswat10.instagramclone.models

data class User (
    val username: String,
    val firstName: String,
    val lastName: String,
    val bio: String,
    val profilePicUrl: String,
    val followerCount: String,
    val followingCount: String,
    val privateAccount: Boolean
)