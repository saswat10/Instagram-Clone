package com.saswat10.instagramclone.domain.models

data class UserObs(
    val userId: String = "",
    val email: String = "",
    val username: String = "",
    val fullName: String = "",
    val bio: String = "",
    val profilePic: String? = null,
    val posts: Int = 0,
    val followerCount: Int = 0,
    val followingCount: Int = 0,
    val privateAccount: Boolean = false,
    val followers: List<UserPreview> = emptyList(),
    val following: List<UserPreview> = emptyList(),
    val sentRequests: List<UserPreview> = emptyList(),
    val pendingRequests: List<UserPreview> = emptyList(),
) {

    // store the user id of the other person
    data class UserPreview(
        val userId: String = "",
        val username: String = "",
        val fullName: String = "",
        val profilePic: String? = null
    )

    fun getname(): String {
        return if (username.length > 10) {
            "@" + username.substring(0, 10) + "..."
        } else if (username.isEmpty()) {
            "no username"
        } else {
            "@$username"
        }
    }
}