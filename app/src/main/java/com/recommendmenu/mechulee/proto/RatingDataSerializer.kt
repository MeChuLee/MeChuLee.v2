package com.recommendmenu.mechulee.proto

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.google.protobuf.InvalidProtocolBufferException
import com.recommendmenu.mechulee.RatingData
import java.io.InputStream
import java.io.OutputStream

// DataStore는 단일 객체로 사용하기 때문에 Serializer역시 object로 선언
object RatingDataSerializer : Serializer<RatingData> {
    override val defaultValue: RatingData = RatingData.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): RatingData {
        try {
            return RatingData.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(
        t: RatingData,
        output: OutputStream) = t.writeTo(output)
}

// 확장 프로퍼티를 사용하여 앱의 Context에 데이터 저장소를 연결하고,
// 해당 데이터 저장소를 통해 RatingData 형식의 데이터를 저장하고 로드할 수 있는 기능을 구현
val Context.ratingDataStore: DataStore<RatingData> by dataStore(
    fileName = "ratingData.pb",
    serializer = RatingDataSerializer
)




