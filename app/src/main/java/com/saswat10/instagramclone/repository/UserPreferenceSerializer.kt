package com.saswat10.instagramclone.repository

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import com.saswat10.instagramclone.datastore.UserPreferences
import java.io.InputStream
import java.io.OutputStream


object UserPreferenceSerializer : Serializer<UserPreferences> {
    override val defaultValue = UserPreferences.getDefaultInstance()
    override suspend fun readFrom(input: InputStream): UserPreferences {
        try {
            return UserPreferences.parseFrom(input)
        } catch (e: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", e)
        }
    }

    override suspend fun writeTo(
        t: UserPreferences,
        output: OutputStream
    ) = t.writeTo(output)
}