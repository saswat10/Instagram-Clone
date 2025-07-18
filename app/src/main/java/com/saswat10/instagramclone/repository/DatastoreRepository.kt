package com.saswat10.instagramclone.repository

import androidx.datastore.core.DataStore
import com.saswat10.instagramclone.datastore.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

class DatastoreRepository @Inject constructor(
    private val protoDataStore: DataStore<UserPreferences>
) {

    val userPreferences: Flow<UserPreferences> = protoDataStore.data
        .catch{exception ->
            throw exception
        }

    suspend fun updateUser(
        uid: String?,
        name: String?,
        username: String?,
        profilePic: String?,
    ) {
        protoDataStore.updateData { current ->
            current.toBuilder()
                .setUid(uid ?: "")
                .setName(name ?: "")
                .setUsername(username ?: "")
                .setProfilePic(profilePic ?: "")
                .build()
        }
    }

    suspend fun clearUser() {
        protoDataStore.updateData { current ->
            current.toBuilder()
                .clearName()
                .clearUid()
                .clearUsername()
                .clearProfilePic()
                .build()
        }
    }

}