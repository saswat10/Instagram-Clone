package com.saswat10.instagramclone.data.repository

import com.saswat10.instagramclone.data.mapper.UserMapper.toDomainUser
import com.saswat10.instagramclone.data.remote.IAuthService
import com.saswat10.instagramclone.domain.models.User
import com.saswat10.instagramclone.domain.repository.IAuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authService: IAuthService
) : IAuthRepository {
    override suspend fun login(
        email: String,
        password: String
    ): Result<User?> {
        return try {
            val result = authService.login(email, password)
            Result.success(result?.toDomainUser())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(
        email: String,
        password: String
    ): Result<User?> {
        return try {
            val result = authService.register(email, password)
            Result.success(result?.toDomainUser())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun logout() {
        authService.logout()
    }

    override fun getCurrentUser(): Result<User?> {
        return try {
            val result = authService.getCurrentUser()
            Result.success(result?.toDomainUser())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun isLoggedIn(): Boolean {
        return getCurrentUser().getOrNull() != null
    }

    override fun observeAuthState(): Flow<User?> {
        return authService.observeCurrentUser().map {
            it?.toDomainUser()
        }
    }
}