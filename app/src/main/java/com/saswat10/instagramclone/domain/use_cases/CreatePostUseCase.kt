package com.saswat10.instagramclone.domain.use_cases

import android.content.Context
import android.net.Uri
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.saswat10.instagramclone.BuildConfig
import com.saswat10.instagramclone.data.model.PostDto
import com.saswat10.instagramclone.di.IoDispatcher
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CreatePostUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) {



    private var totalFilesToUpload = 0
    private val uploadedFilesCount = AtomicInteger(0)
    private val uploadedUrls = mutableListOf<String>()


    private var overallUploadCallback: ((List<String>) -> Unit)? = null
    private var overallErrorCallback: ((ErrorInfo) -> Unit)? = null
    private var overallProgressCallback: ((Int) -> Unit)? = null

    fun setUploadCallbacks(
        onSuccess: (List<String>) -> Unit,
        onError: (ErrorInfo) -> Unit,
        onProgress: (Int) -> Unit = {}
    ) {
        this.overallUploadCallback = onSuccess
        this.overallErrorCallback = onError
        this.overallProgressCallback = onProgress
    }

    suspend fun uploadFiles(uris: List<Uri>) {
        withContext(ioDispatcher) {
            uploadedUrls.clear()
            totalFilesToUpload = uris.size
            uploadedFilesCount.set(0)

            if (uris.isEmpty()) {
                return@withContext
            }
            uris.forEachIndexed {index, uri ->
                val resourceType = getResourceType(uri)
                uploadToCloudinary(uri, resourceType)
            }
        }
    }

    fun uploadToCloudinary(uri: Uri, resourceType: String) {
        MediaManager.get().upload(uri).unsigned(BuildConfig.CLOUDINARY_UPLOAD_PRESET)
            .option("folder", "posts")
            .option("resource_type", resourceType)
            .callback(object : UploadCallback {
                override fun onStart(requestId: String?) {
                    Timber.d("Upload started for $uri .....")
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
                    uploadedFilesCount.incrementAndGet()
                    val url = resultData?.get("secure_url") as? String
                    if(url != null){
                        uploadedUrls.add(url)
                    }
                    if(totalFilesToUpload == uploadedFilesCount.get()){
                        overallUploadCallback?.invoke(uploadedUrls)
                    }
                }

                override fun onError(
                    requestId: String?,
                    error: ErrorInfo?
                ) {
                    Timber.e(error?.description)
                    overallErrorCallback?.invoke(error!!)
                }

                override fun onReschedule(
                    requestId: String?,
                    error: ErrorInfo?
                ) {
                }
            }).dispatch(context)

    }


    private fun getResourceType(uri: Uri): String {
        return if (context.contentResolver.getType(uri)?.startsWith("video") == true) {
            "video"
        } else {
            "image"
        }
    }
}