package com.saswat10.instagramclone.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.saswat10.instagramclone.utils.FirebaseConstantsV2
import javax.inject.Inject

class PostRepository @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {

    private val postCollection = fireStore.collection(FirebaseConstantsV2.Posts.COLLECTION_POSTS)
    private val currentUser = auth.currentUser
}