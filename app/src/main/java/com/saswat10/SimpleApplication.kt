package com.saswat10

import android.app.Application
import com.cloudinary.android.MediaManager
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

        val config = HashMap<String, String>().apply {
            put("cloud_name", BuildConfig.CLOUDINARY_CLOUD_NAME)
            put("api_key", BuildConfig.CLOUDINARY_API_KEY)
            put("api_secret", BuildConfig.CLOUDINARY_API_SECRET)
            put("upload_preset", BuildConfig.CLOUDINARY_UPLOAD_PRESET)

        }
        MediaManager.init(this, config)
    }
}