package com.moritoui.vegegrowthapp.core.analytics

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AnalyticsHelperModule {
    @Binds
    @Singleton
    abstract fun provideAnalyticsHelper(
        impl: AnalyticsHelperImpl
    ): AnalyticsHelper
}
