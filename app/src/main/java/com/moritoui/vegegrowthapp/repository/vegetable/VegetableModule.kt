package com.moritoui.vegegrowthapp.repository.vegetable

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class VegetableModule {
    @Binds
    abstract fun provideVegetableRepository(vegetableRepositoryImpl: VegetableRepositoryImpl): VegetableRepository
}
