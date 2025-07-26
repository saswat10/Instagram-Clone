package com.saswat10.instagramclone.domain.use_cases

import com.saswat10.instagramclone.data.UserDatastoreRepository
import com.saswat10.instagramclone.domain.models.User
import com.saswat10.instagramclone.domain.repository.IAuthRepository
import com.saswat10.instagramclone.domain.repository.IUserRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: IAuthRepository,
    private val userRepository: IUserRepository,
    private val userPref: UserDatastoreRepository
) {

    suspend operator fun invoke(email: String, password: String): Result<User?> {
        if (email.isBlank() || password.isBlank()) {
            return Result.failure(IllegalArgumentException("Email or password cannot be empty"))
        }


        val authResult = authRepository.login(email, password).getOrNull()
        val uid = authResult?.userId
        if (uid == null) return Result.failure(Exception("Null"))
        val dbResult = userRepository.getUserById(uid).getOrNull()

        return Result.success(dbResult)
    }
}