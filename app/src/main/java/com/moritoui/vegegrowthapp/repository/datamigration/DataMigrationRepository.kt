package com.moritoui.vegegrowthapp.repository.datamigration

import kotlinx.coroutines.flow.MutableStateFlow

interface DataMigrationRepository {
    suspend fun dataMigration()

    var isDataMigrating: MutableStateFlow<Boolean>
}
