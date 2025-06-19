package com.saswat10.instagramclone.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.saswat10.instagramclone.models.remote.RemoteUser
import timber.log.Timber
import javax.inject.Inject

class FirestoreRepository @Inject constructor(private val firestore: FirebaseFirestore) {

    suspend fun createUser(user: RemoteUser, uid: String) {
        firestore.collection("users").document(uid).set(user)
            .addOnSuccessListener {
                Timber.tag("FirestoreRepository").d("User created successfully")
            }.addOnFailureListener {
                Timber.tag("ERROR").d(it.localizedMessage)
            }
    }
}