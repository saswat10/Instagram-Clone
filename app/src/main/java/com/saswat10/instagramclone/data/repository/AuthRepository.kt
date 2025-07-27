package com.saswat10.instagramclone.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.saswat10.instagramclone.data.mapper.UserMapper.toDomainUser
import com.saswat10.instagramclone.data.remote.IAuthService
import com.saswat10.instagramclone.domain.models.User
import com.saswat10.instagramclone.domain.repository.IAuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val authService: IAuthService
) : IAuthRepository {
    override suspend fun login(
        email: String,
        password: String
    ): Result<User?> {
        return runCatching {
            val result = authService.login(email, password)
            if (result == null) {
                throw IllegalStateException("Login operation returned a null result.")
            }
            result.toDomainUser()
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

    override fun observeAuthState(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
        }
}