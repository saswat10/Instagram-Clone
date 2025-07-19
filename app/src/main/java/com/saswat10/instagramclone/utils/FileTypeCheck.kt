package com.saswat10.instagramclone.utils

import android.content.ContentResolver
import android.net.Uri


fun isImage(contentResolver: ContentResolver, uri: Uri): Boolean {
    val mimeType = contentResolver.getType(uri)
    return mimeType != null && mimeType.startsWith("image/")
}

fun isVideo(contentResolver: ContentResolver, uri: Uri): Boolean {
    val mimeType = contentResolver.getType(uri)
    return mimeType != null && mimeType.startsWith("video/")
}

