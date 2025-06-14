package com.saswat10

import android.app.Application
import com.saswat10.instagramclone.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class SimpleApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        if(BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())
        else
            Timber.uproot(Timber.DebugTree())
    }
}