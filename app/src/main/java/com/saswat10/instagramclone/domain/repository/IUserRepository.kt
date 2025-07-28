package com.saswat10.instagramclone.domain.repository

import com.saswat10.instagramclone.data.model.UserDto
import com.saswat10.instagramclone.domain.models.User
import kotlinx.coroutines.flow.Flow

interface IUserRepository {
    suspend fun observeUserById(uid: String): Flow<Result<User?>>
    suspend fun getUserById(uid: String): Result<User?>

    suspend fun createUser(uid: String, user: User): Result<Unit>

}