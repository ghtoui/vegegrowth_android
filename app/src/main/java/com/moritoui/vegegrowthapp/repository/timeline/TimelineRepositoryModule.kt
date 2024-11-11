package com.moritoui.vegegrowthapp.repository.timeline

import com.moritoui.vegegrowthapp.repository.timeline.impl.TimelineRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class TimelineRepositoryModule {
    @Binds
    abstract fun provideTimelineRepository(timelineRepositoryImpl: TimelineRepositoryImpl): TimelineRepository
}
