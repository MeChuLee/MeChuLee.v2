package com.recommendmenu.mechulee.proto

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.google.protobuf.InvalidProtocolBufferException
import com.recommendmenu.mechulee.IngredientData
import java.io.InputStream
import java.io.OutputStream

object IngredientDataSerializer : Serializer<IngredientData> {
    override val defaultValue: IngredientData = IngredientData.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): IngredientData {
        try {
            return IngredientData.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(
        t: IngredientData,
        output: OutputStream
    ) = t.writeTo(output)
}

val Context.checkedIngredientDataStore: DataStore<IngredientData> by dataStore(
    fileName = "ingredientData.pb",
    serializer = IngredientDataSerializer
)
