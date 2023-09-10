package com.recommendmenu.mechulee.proto

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.google.protobuf.InvalidProtocolBufferException
import com.recommendmenu.mechulee.LikeData
import java.io.InputStream
import java.io.OutputStream

object LikeDataSerializer : Serializer<LikeData> {
    override val defaultValue: LikeData = LikeData.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): LikeData {
        try {
            return LikeData.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(
        t: LikeData,
        output: OutputStream
    ) = t.writeTo(output)
}

val Context.likedMenuDataStore: DataStore<LikeData> by dataStore(
    fileName = "likeData.pb",
    serializer = LikeDataSerializer
)
