package com.moritoui.vegegrowthapp.data.datastores.migrate

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class MigratePreferencesSerializer
@Inject
constructor() : Serializer<MigratePreferences> {
    override val defaultValue: MigratePreferences = MigratePreferences(false)

    override suspend fun readFrom(input: InputStream): MigratePreferences {
        try {
            return Json.decodeFromString(
                MigratePreferences.serializer(),
                input.readBytes().decodeToString()
            )
        } catch (serialization: SerializationException) {
            throw CorruptionException("migratePreferencesが読み込めませんでした", serialization)
        }
    }

    override suspend fun writeTo(t: MigratePreferences, output: OutputStream) {
        output.write(
            Json.encodeToString(MigratePreferences.serializer(), t).encodeToByteArray()
        )
    }
}
