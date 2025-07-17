package com.saswat10.instagramclone.utils

class FirebaseConstantsV2 {

    object Users {
        const val COLLECTION_USERS = "users"
        const val ID = "id"
        const val FIELD_USERNAME = "username"
        const val FIELD_NAME = "name"
        const val FIELD_FRIENDS = "friends"
        const val FIELD_POSTS = "posts"

    }

    object Friends {
        const val ID = "id"
        const val COLLECTION_FRIENDS = "friends"
        const val FIELD_USER_IDS = "userIds"
        const val CREATED_AT = "createdAt"

    }

    object Requests {
        const val COLLECTION_REQUESTS = "requests"
        const val FIELD_SENDER = "fromId"
        const val FIELD_RECEIVER = "toId"
        const val FIELD_STATUS = "status"
        const val FIELD_CREATED_AT = "createdAt"
    }

    object Posts {
        const val COLLECTION_POSTS = "posts"
        const val ID = "id"
        const val FIELD_USERNAME = "username"
        const val FIELD_USER_ID = "userId"
        const val FIELD_CREATED_AT = "createdAt"
        const val FIELD_UPDATED_AT = "updatedAt"

        const val SUBCOLLECTION_COMMENTS = "comments"
        const val SUBCOLLECTION_LIKES = "likes"
    }

    object Chats {
        const val ID = "id"
        const val COLLECTION_CHATS = "chats"
        const val FIELD_USER_IDS = "userIds"

        const val SUBCOLLECTION_MESSAGES = "messages"
    }
}