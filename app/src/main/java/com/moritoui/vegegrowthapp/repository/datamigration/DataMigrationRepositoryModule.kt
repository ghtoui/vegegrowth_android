package com.moritoui.vegegrowthapp.repository.datamigration

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class DataMigrationRepositoryModule {
    @Binds
    abstract fun provideDataMigrationRepository(impl: DataMigrationRepositoryImpl): DataMigrationRepository
}
