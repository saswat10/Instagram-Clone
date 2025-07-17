package com.saswat10.instagramclone.utils

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object SnackBarManager {
    private val _messages = MutableSharedFlow<String>(
        replay = 0,
        extraBufferCapacity = 1
    )
    val messages = _messages.asSharedFlow()

    suspend fun showMessage(message: String) {
        _messages.emit(message)
    }
}
