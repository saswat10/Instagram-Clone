package com.saswat10.instagramclone.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.saswat10.instagramclone.datastore.UserPreferences
import com.saswat10.instagramclone.repository.UserPreferenceSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UserPreferencesModule {
    companion object{
        private const val USER_PREFERENCES_NAME = "user_preferences"
        private const val DATA_STORE_FILE_NAME = "user_pref.pb"
    }

    @Provides
    @Singleton
    fun provideProtoDatastore(@ApplicationContext context: Context): DataStore<UserPreferences> {
        return DataStoreFactory.create(
            serializer = UserPreferenceSerializer,
            produceFile = {context.dataStoreFile(DATA_STORE_FILE_NAME)}
        )
    }
}