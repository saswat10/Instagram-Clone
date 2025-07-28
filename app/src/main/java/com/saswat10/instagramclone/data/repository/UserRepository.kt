package com.saswat10.instagramclone.data.repository

import com.saswat10.instagramclone.data.UserDatastoreRepository
import com.saswat10.instagramclone.data.mapper.UserMapper.toDomainUser
import com.saswat10.instagramclone.data.mapper.UserMapper.toUserDto
import com.saswat10.instagramclone.data.model.PostDto
import com.saswat10.instagramclone.data.remote.IPostService
import com.saswat10.instagramclone.data.remote.IUserService
import com.saswat10.instagramclone.domain.models.User
import com.saswat10.instagramclone.domain.repository.IUserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userService: IUserService,
    private val postService: IPostService,
    private val userDatastoreRepository: UserDatastoreRepository
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
                userDatastoreRepository.saveUser(
                    user.userId, user.name, user.username,
                    user.profilePic
                )
            }
        }
    }

    override suspend fun createUser(uid: String, user: User): Result<Unit> {
        return runCatching {
            userService.createNewUser(uid, user.toUserDto())
        }
    }


    override suspend fun createPost(
        urls: List<String>,
        typeString: String,
        captionString: String,
    ): Result<Unit> {
        return runCatching {
            val mediaList = toPostMediaDto(urls, typeString)
            val postDto = PostDto(
                username = _user.value.username,
                userId = _user.value.userId,
                profilePic = _user.value.profilePic,
                caption = captionString,
                media = mediaList,
            )
            postService.createPost(postDto).onSuccess {
                _user.update { it.copy(posts = it.posts + 1) }
            }
        }
    }

    override fun clearUser() {
        _user.value = User()
    }


    fun toPostMediaDto(list: List<String>, type: String): List<PostDto.MediaDto>{
        return list.mapIndexed {index, url ->
            PostDto.MediaDto(index.toString(), url, type)
        }
    }
}
