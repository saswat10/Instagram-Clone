package com.saswat10.instagramclone.data.mapper

import com.google.firebase.auth.FirebaseUser
import com.saswat10.instagramclone.data.model.UserDto
import com.saswat10.instagramclone.domain.models.User
import java.time.ZoneId

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
            email = this.email,
            createdAt = this.createdAt?.toDate()?.toInstant()?.atZone(ZoneId.systemDefault())
                ?.toLocalDateTime(),
        )
    }

    fun FirebaseUser.toDomainUser(): User {
        return User(
            userId = this.uid,
            email = this.email,
        )
    }
}