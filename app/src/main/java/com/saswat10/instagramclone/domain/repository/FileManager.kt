package com.saswat10.instagramclone.domain.repository

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FileManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun uriToByteArray(contentUri: Uri): ByteArray{
        return withContext(ioDispatcher){
            val bytes = context
                .contentResolver
                .openInputStream(contentUri)
                ?.use {inputStream ->
                    inputStream.readBytes()
                }?: byteArrayOf()

            bytes
        }
    }
}