package com.moritoui.vegegrowthapp.di

import android.content.Context
import com.moritoui.vegegrowthapp.model.FileManager
import com.moritoui.vegegrowthapp.model.FileManagerImpl
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
    fun provideFileManager(@ApplicationContext context: Context): FileManager {
        return FileManagerImpl(context)
    }

    @Provides
    @Singleton
    fun provideVegeItemRepository(fileManger: FileManager): VegeItemListRepository {
        return VegeItemListRepositoryImpl(fileManger)
    }
}
