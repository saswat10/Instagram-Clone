package com.saswat10.instagramclone.data.mapper

import com.google.firebase.auth.FirebaseUser
import com.saswat10.instagramclone.data.model.UserDto
import com.saswat10.instagramclone.domain.models.User

object UserMapper {
    fun UserDto.toDomainUser(): User {
        return User(
            name = this.name,
            username = this.username,
            userId = this.id,
            posts = this.posts,
            friends = this.friends,
            bio = this.bio,
            profilePic = this.profilePic,
        )
    }

    fun FirebaseUser.toDomainUser(): User {
        return User(
            email = this.email,
            profilePic = this.photoUrl?.toString(),
            name = this.displayName ?: "",
        )
    }

    fun User.merge(fsUser: User?, authUser: User): User {
        require(fsUser == null || authUser.userId == fsUser.userId) { "Mismatched UIDs during merge" }

        return User(
            userId = authUser.userId,
            name = authUser.name,
            profilePic = authUser.profilePic,
            username = fsUser?.username ?: "",
            posts = fsUser?.posts ?: 0,
            friends = fsUser?.friends ?: 0,
            bio = fsUser?.bio ?: "",
        )
    }
}