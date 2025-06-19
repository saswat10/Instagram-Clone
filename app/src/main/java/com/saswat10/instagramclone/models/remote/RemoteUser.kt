package com.saswat10.instagramclone.models.remote

data class RemoteUser (
    val username: String = "",
    val email: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val bio: String = "",
    val followerCount: Int = 0,
    val followingCount: Int = 0,
    val private: Boolean = false,
    val posts: Int = 0,
    val profilePic: String = ""
)