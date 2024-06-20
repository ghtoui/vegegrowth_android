package com.moritoui.vegegrowthapp.di

import android.content.Context
import com.moritoui.vegegrowthapp.data.room.dao.VegetableDao
import com.moritoui.vegegrowthapp.model.DateFormatter
import com.moritoui.vegegrowthapp.model.FileManager
import com.moritoui.vegegrowthapp.model.FileManagerImpl
import com.moritoui.vegegrowthapp.model.VegetableRepositoryFileManager
import com.moritoui.vegegrowthapp.repository.VegeItemDetailRepository
import com.moritoui.vegegrowthapp.repository.VegeItemDetailRepositoryImpl
import com.moritoui.vegegrowthapp.repository.VegeItemListRepository
import com.moritoui.vegegrowthapp.repository.VegeItemListRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Modules {
    @Provides
    @Singleton
    fun provideFileManager(@ApplicationContext context: Context): FileManager = FileManagerImpl(context)

    @Provides
    @Singleton
    fun provideVegeItemRepository(fileManger: FileManager, vegetableDao: VegetableDao): VegeItemListRepository = VegeItemListRepositoryImpl(fileManger, vegetableDao)

    @Provides
    @Singleton
    fun provideDateFormatter(): DateFormatter = DateFormatter()

    @Provides
    fun provideVegeDetailRepository(fileManger: VegetableRepositoryFileManager): VegeItemDetailRepository = VegeItemDetailRepositoryImpl(fileManger)
}
