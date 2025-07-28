package com.saswat10.instagramclone.data.repository

import com.saswat10.instagramclone.data.mapper.UserMapper.toDomainUser
import com.saswat10.instagramclone.data.mapper.UserMapper.toUserDto
import com.saswat10.instagramclone.data.remote.IUserService
import com.saswat10.instagramclone.domain.models.User
import com.saswat10.instagramclone.domain.repository.IUserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userService: IUserService
) : IUserRepository {

    private val _user = MutableStateFlow(User())
    val user = _user.asStateFlow()

    override suspend fun observeUserById(uid: String): Flow<Result<User?>> {
        val userFlow = userService.observeUser(uid)
        return userFlow.map { result ->
            result.map { userDto ->
                userDto?.toDomainUser()
            }.onSuccess { user ->
                if (user != null) {
                    _user.value = user
                }
            }
        }
    }

    override suspend fun getUserById(uid: String): Result<User?> {
        return userService.getUserByUid(uid).map {
            it?.toDomainUser()
        }.onSuccess { user ->
            if (user != null) {
                _user.value = user
            }
        }
    }

    override suspend fun createUser(uid: String, user: User): Result<Unit> {
        return runCatching {
            userService.createNewUser(uid, user.toUserDto())
        }
    }
}