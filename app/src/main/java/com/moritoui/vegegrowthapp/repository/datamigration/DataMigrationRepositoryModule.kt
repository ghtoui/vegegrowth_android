package com.moritoui.vegegrowthapp.repository.datamigration

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataMigrationRepositoryModule {
    @Binds
    abstract fun provideDataMigrationRepository(impl: DataMigrationRepositoryImpl): DataMigrationRepository
}
