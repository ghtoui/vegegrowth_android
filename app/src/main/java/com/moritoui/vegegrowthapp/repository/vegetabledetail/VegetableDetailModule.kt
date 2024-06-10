package com.moritoui.vegegrowthapp.repository.vegetabledetail

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class VegetableDetailModule {
    @Binds
    abstract fun provideVegetableDetailRepository(vegetableDetailRepositoryImpl: VegetableDetailRepositoryImpl): VegetableDetailRepository
}
