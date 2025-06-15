package com.saswat10.instagramclone.models

import com.google.firebase.auth.FirebaseUser

data class User(
    val userId: String="",
    val email: String = "",
    val username: String = "",
    val fullName: String = "",
    val bio: String = "",
    val profilePicUrl: String = "",
    val followerCount: String = "",
    val followingCount: String = "",
    val privateAccount: Boolean = false,
    val followers: List<FollowEntry> = emptyList(),
    val following: List<FollowEntry> = emptyList(),
    val sentRequests: List<FriendRequestEntry> = emptyList(),
    val pendingRequests: List<FriendRequestEntry> = emptyList()
) {

    // store the user id of the other person
    data class FollowEntry(
        val userId: String = "",
        val username: String = "",
        val profilePicUrl: String = ""
    )

    // store the user id of the other person
    data class FriendRequestEntry(
        val userId: String = "",
        val username: String = "",
        val profilePicUrl: String = ""
    )
}
