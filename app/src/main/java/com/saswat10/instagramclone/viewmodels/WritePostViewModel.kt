package com.saswat10.instagramclone.viewmodels

import com.saswat10.instagramclone.repository.CloudinaryRepository
import com.saswat10.instagramclone.repository.FirebaseAuthRepository
import com.saswat10.instagramclone.repository.PostRepository
import javax.inject.Inject

class WritePostViewModel @Inject constructor(
    private val cloudinaryRepository: CloudinaryRepository,
    private val postRepository: PostRepository,
    private val authRepository: FirebaseAuthRepository
) {
}