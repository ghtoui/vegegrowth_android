package com.moritoui.vegegrowthapp.repository.datamigration

interface DataMigrationRepository {
    suspend fun dataMigration()
}

