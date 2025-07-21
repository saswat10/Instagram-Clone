package com.saswat10.instagramclone.data.repository

import com.saswat10.instagramclone.data.mapper.UserMapper.toDomainUser
import com.saswat10.instagramclone.data.remote.IUserService
import com.saswat10.instagramclone.domain.models.User
import com.saswat10.instagramclone.domain.repository.IUserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userService: IUserService
) : IUserRepository {

    override suspend fun observeUserById(uid: String): Flow<Result<User?>> {
        val userFlow = userService.observeUser(uid)
        return userFlow.map { result ->
            result.map { userDto ->
                userDto?.toDomainUser()
            }
        }
    }

    override suspend fun getUserById(uid: String): Result<User?> {
        return userService.getUserByUid(uid).map {
            it?.toDomainUser()
        }
    }
}