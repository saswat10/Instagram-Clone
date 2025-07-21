package com.saswat10.instagramclone.domain.use_cases

import com.saswat10.instagramclone.data.mapper.UserMapper.merge
import com.saswat10.instagramclone.domain.models.User
import com.saswat10.instagramclone.domain.repository.IAuthRepository
import com.saswat10.instagramclone.domain.repository.IUserRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: IAuthRepository,
    private val userRepository: IUserRepository
) {

    suspend operator fun invoke(email: String, password: String): Result<User?> {
        if (email.isBlank() || password.isBlank()) {
            return Result.failure(IllegalArgumentException("Email or password cannot be empty"))
        }


        val authResult = authRepository.login(email, password)
        return authResult.onSuccess { authUser ->
            if (authUser == null) {
                return Result.failure(Exception("Null user from Firebase Auth"))
            }
            val profileFetch = userRepository.getUserById(authUser.userId)
            profileFetch.onSuccess { dbUser ->
                Result.success(dbUser?.merge(dbUser, authUser))
            }.onFailure {
                Result.failure<Throwable>(Exception("Failed to fetch User profile from Firestore"))
            }
        }.onFailure {
            return@onFailure
        }
    }
}