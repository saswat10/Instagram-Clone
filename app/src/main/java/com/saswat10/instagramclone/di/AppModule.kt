package com.saswat10.instagramclone.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.saswat10.instagramclone.data.remote.IAuthService
import com.saswat10.instagramclone.data.remote.IFriendRequestService
import com.saswat10.instagramclone.data.remote.IPostService
import com.saswat10.instagramclone.data.remote.IUserService
import com.saswat10.instagramclone.data.remote.firebase.AuthService
import com.saswat10.instagramclone.data.remote.firebase.FriendRequestService
import com.saswat10.instagramclone.data.remote.firebase.PostService
import com.saswat10.instagramclone.data.remote.firebase.UserService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAuthService(firebaseAuth: FirebaseAuth): IAuthService {
        return AuthService(firebaseAuth)
    }

    @Provides
    @Singleton
    fun provideUserService(firestore: FirebaseFirestore): IUserService {
        return UserService(firestore)
    }

    @Provides
    @Singleton
    fun provideFriendRequestService(firestore: FirebaseFirestore): IFriendRequestService {
        return FriendRequestService(firestore)
    }

    @Provides
    @Singleton
    fun providePostService(firestore: FirebaseFirestore): IPostService {
        return PostService(firestore)
    }

}