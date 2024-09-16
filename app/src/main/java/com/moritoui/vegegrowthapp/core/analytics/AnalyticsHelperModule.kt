package com.moritoui.vegegrowthapp.core.analytics

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AnalyticsHelperModule {
    @Binds
    @Provides
    abstract fun provideAnalyticsHelper(
        impl: AnalyticsHelperImpl
    ): AnalyticsHelper
}
