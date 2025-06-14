package com.saswat10.instagramclone.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject


class FirebaseAuthRepository @Inject constructor(private val auth: FirebaseAuth){

    var currentUser: FirebaseUser? = auth.currentUser

    fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
    }
}