package com.saswat10.instagramclone.repository

import android.content.Context
import android.net.Uri
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.saswat10.instagramclone.BuildConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class CloudinaryRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun uploadImage(imageFile: Uri, folder: String, onResult: (Result<String>) -> Unit) {
        MediaManager.get().upload(imageFile)
            .unsigned(BuildConfig.CLOUDINARY_UPLOAD_PRESET)
            .option("folder", folder)
            .callback(
                object : UploadCallback {
                    override fun onStart(requestId: String?) {
                    }

                    override fun onProgress(
                        requestId: String?,
                        bytes: Long,
                        totalBytes: Long
                    ) {
                    }

                    override fun onSuccess(
                        requestId: String?,
                        resultData: Map<*, *>?
                    ) {
                        val originalUrl = resultData?.get("url") as? String
                        if (originalUrl != null) {
                            val secureUrl = ensureHttps(originalUrl)
                            onResult(Result.success(secureUrl))
                        } else {
                            onResult(Result.failure(RuntimeException("Cloudinary URL not found in result data.")))
                        }
                    }

                    override fun onError(
                        requestId: String?,
                        error: ErrorInfo?
                    ) {
                    }

                    override fun onReschedule(
                        requestId: String?,
                        error: ErrorInfo?
                    ) {
                    }

                }
            )
            .dispatch(context)
    }


    private fun ensureHttps(url: String): String {
        return when {
            url.startsWith("https://", ignoreCase = true) -> url
            url.startsWith("http://", ignoreCase = true) -> url.replaceFirst("http://", "https://", ignoreCase = true)
            // Handle URLs that might not have a scheme (e.g., //res.cloudinary.com/...)
            url.startsWith("//") -> "https:$url"
            // If it's a relative path or something else entirely, just prepend https://
            else -> "https://$url"
        }
    }

}