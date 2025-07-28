package com.saswat10.instagramclone.domain.use_cases

import com.saswat10.instagramclone.data.UserDatastoreRepository
import com.saswat10.instagramclone.domain.models.User
import com.saswat10.instagramclone.domain.repository.IAuthRepository
import com.saswat10.instagramclone.domain.repository.IUserRepository
import com.saswat10.instagramclone.utils.flatMap
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: IAuthRepository,
    private val userRepository: IUserRepository,
    private val userPreferencesRepository: UserDatastoreRepository
) {

    suspend fun invoke(
        name: String,
        username: String,
        email: String,
        password: String,
    ): Result<Unit> {
        return runCatching {
            authRepository.register(email, password)
                .flatMap { user ->
                    if (user != null) {
                        val userRef = User(
                            userId = user.userId,
                            email = user.email,
                            name = name,
                            username = username,
                        )
                        userRepository.createUser(user.userId, userRef).onSuccess {
                            userPreferencesRepository.saveUser(user.userId, name, username, "")
                        }.onSuccess {
                            userRepository.getUserById(user.userId)
                        }
                    } else {
                        Result.failure(IllegalArgumentException(""))
                    }
                }
        }
    }
}