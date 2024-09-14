package com.moritoui.vegegrowthapp.data.datastores.appsettings

import kotlinx.serialization.Serializable

@Serializable
data class AppSettingsPreferences(
    val isInitialStartApp: Boolean
) {
    companion object {
        fun initial() : AppSettingsPreferences = AppSettingsPreferences(
            isInitialStartApp = true
        )
    }
}
