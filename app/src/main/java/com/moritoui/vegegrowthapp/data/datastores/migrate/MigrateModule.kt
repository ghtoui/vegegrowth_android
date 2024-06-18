package com.moritoui.vegegrowthapp.data.datastores.migrate

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MigrateModule {
    @Singleton
    @Provides
    fun provideMigratePreferencesDataStore(@ApplicationContext context: Context): DataStore<MigratePreferences> = DataStoreFactory.create(
        serializer = MigratePreferencesSerializer(),
        produceFile = { context.preferencesDataStoreFile("migrate.pb") }
    )
}
