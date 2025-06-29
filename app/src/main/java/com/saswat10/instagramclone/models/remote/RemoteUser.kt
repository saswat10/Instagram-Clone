package com.saswat10.instagramclone.models.remote

import com.google.firebase.firestore.DocumentId
import com.saswat10.instagramclone.models.domain.User

data class RemoteUser(
    @DocumentId
    val uid: String = "",
    val username: String = "",
    val email: String = "",
    val fullName: String = "",
    val bio: String = "",
    val followerCount: Int = 0,
    val followingCount: Int = 0,
    val private: Boolean = false,
    val posts: Int = 0,
    val profilePic: String = ""
)

fun RemoteUser.toUser(): User {
    return User(
        userId = this.uid,
        username = this.username,
        email = this.email,
        fullName = this.fullName,
        bio = this.bio,
        followerCount = this.followerCount,
        followingCount = this.followingCount,
        privateAccount = this.private,
        profilePic = this.profilePic,
        followers = emptyList(),
        following = emptyList(),
        sentRequests = emptyList(),
        pendingRequests = emptyList(),
        posts = posts
    )
}