package com.saswat10.instagramclone.data

import androidx.datastore.core.DataStore
import com.saswat10.instagramclone.datastore.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import okio.IOException
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDatastoreRepository @Inject constructor(
    private val dataStore: DataStore<UserPreferences>
) {

    val userPreferencesFlow: Flow<UserPreferences> = dataStore.data.
        catch { e->
            if(e is IOException){
                Timber.e("UserPreferencesRepo, Error reading preferences.")
                emit(UserPreferences.getDefaultInstance())
            }else{
                throw e
            }
        }

    suspend fun updateId(id: String) {
        dataStore.updateData { currentPreferences ->
            currentPreferences.toBuilder()
                .setUid(id)
                .build()
        }
    }

    suspend fun updateUsername(username: String) {
        dataStore.updateData { currentPreferences ->
            currentPreferences.toBuilder()
                .setUsername(username)
                .build()
        }
    }

    suspend fun updateProfilePic(profilePic: String) {
        dataStore.updateData { currentPreferences ->
            currentPreferences.toBuilder()
                .setProfilePic(profilePic)
                .build()
        }
    }

    suspend fun updateName(name: String) {
        dataStore.updateData { currentPreferences ->
            currentPreferences.toBuilder()
                .setName(name)
                .build()
        }
    }

    suspend fun clearAll() {
        dataStore.updateData { currentPreferences ->
            currentPreferences.toBuilder()
                .clearUid()
                .clearName()
                .clearUsername()
                .clearProfilePic()
                .build()
        }
    }

}