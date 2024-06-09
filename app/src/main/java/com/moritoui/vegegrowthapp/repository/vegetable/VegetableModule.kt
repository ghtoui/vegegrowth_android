package com.moritoui.vegegrowthapp.repository.vegetable

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class VegetableModule {
    @Binds
    abstract fun provideVegetableRepository(vegetableRepositoryImpl: VegetableRepositoryImpl): VegetableRepository
}
