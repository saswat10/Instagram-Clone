package com.saswat10.instagramclone.utils

object FirebaseConstants {
    
    // collections
    const val COLLECTION_USERS = "users"
    const val COLLECTION_POSTS = "posts"

    /*
     ********************
     *   USER DOCUMENT  *
     ********************
     */
    // user doc fields
    const val FIELD_USERNAME = "username"
    const val FIELD_EMAIL = "email"
    const val FIELD_FULL_NAME = "fullName"
    const val FIELD_BIO = "bio"
    const val FIELD_FOLLOWERS = "followerCount"
    const val FIELD_FOLLOWING = "followingCount"
    const val FIELD_PRIVATE = "private"
    const val FIELD_POSTCOUNT = "posts"
    const val FIELD_PROFILE_PIC = "profilePic"

    // user subcollections users/{uid}
    const val SUBCOLLECTIONS_FOLLOWERS = "followers"
    const val SUBCOLLECTIONS_FOLLOWING = "following"
    const val SUBCOLLECTIONS_SENT_REQUESTS = "sent_requests"
    const val SUBCOLLECTIONS_PENDING_ACCEPTS = "pending_accepts"

    // user fields within subcollections users/{uid}
    const val SUBCOLLECTIONS_FIELD_UID = "uid"
    const val SUBCOLLECTIONS_FIELD_USERNAME = "username"
    const val SUBCOLLECTIONS_FULL_NAME = "fullName"
    const val SUBCOLLECTIONS_FIELD_PROFILE_PIC = "profilePic"

    /*
     ********************
     *   POST DOCUMENT  *
     ********************
     */

    // post doc fields


}