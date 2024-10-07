package com.moritoui.vegegrowthapp.data.datastores.appsettings

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import java.io.InputStream
import java.io.OutputStream
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import javax.inject.Inject

class AppSettingsPreferencesSerializer @Inject constructor() : Serializer<AppSettingsPreferences> {
    override val defaultValue: AppSettingsPreferences = AppSettingsPreferences()

    override suspend fun readFrom(input: InputStream): AppSettingsPreferences {
        try {
            return Json.decodeFromString(
                AppSettingsPreferences.serializer(),
                input.readBytes().decodeToString()
            )
        } catch (serialization: SerializationException) {
            throw CorruptionException("appSettingsPreferencesが読み込めませんでした", serialization)
        }
    }

    override suspend fun writeTo(t: AppSettingsPreferences, output: OutputStream) {
        output.write(
            Json.encodeToString(AppSettingsPreferences.serializer(), t).encodeToByteArray()
        )
    }
}
