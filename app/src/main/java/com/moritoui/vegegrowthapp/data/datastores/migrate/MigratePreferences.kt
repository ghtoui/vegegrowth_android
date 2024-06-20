package com.moritoui.vegegrowthapp.data.datastores.migrate

import kotlinx.serialization.Serializable

@Serializable
data class MigratePreferences(val isMigrated: Boolean)
