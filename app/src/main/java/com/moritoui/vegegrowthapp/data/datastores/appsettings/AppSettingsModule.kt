package com.moritoui.vegegrowthapp.data.datastores.appsettings

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
class AppSettingsModule {
    @Singleton
    @Provides
    fun provideAppSettingsPreferencesDataStore(
        @ApplicationContext context: Context
    ) : DataStore<AppSettingsPreferences> = DataStoreFactory.create(
        serializer = AppSettingsPreferencesSerializer(),
        produceFile = { context.preferencesDataStoreFile("appSettings.pb") }
    )
}
