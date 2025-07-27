package com.saswat10.instagramclone.domain.use_cases

import com.saswat10.instagramclone.data.UserDatastoreRepository
import com.saswat10.instagramclone.domain.models.User
import com.saswat10.instagramclone.domain.repository.IAuthRepository
import com.saswat10.instagramclone.domain.repository.IUserRepository
import com.saswat10.instagramclone.utils.flatMap
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

        return authRepository.login(email, password)
            .flatMap { authResult ->
                val uid = authResult?.userId
                if (uid != null) {
                    userRepository.getUserById(uid)
                } else {
                    Result.failure(Exception("Authentication successful but user ID is null"))
                }
            }.onSuccess { user ->
                user?.let { userPref.saveUser(it.userId, it.name, it.username, it.profilePic) }
            }.onFailure {
                return@onFailure
            }


//        val authResult = authRepository.login(email, password).getOrNull()
//        val uid = authResult?.userId
//        if (uid == null) return Result.failure(Exception("Null"))
//        val dbResult = userRepository.getUserById(uid).getOrNull()
//
//        Result.success(dbResult)


    }
}