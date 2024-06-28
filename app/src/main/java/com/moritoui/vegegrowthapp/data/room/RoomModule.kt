package com.moritoui.vegegrowthapp.data.room

import android.content.Context
import androidx.room.Room
import com.moritoui.vegegrowthapp.data.room.dao.VegetableDao
import com.moritoui.vegegrowthapp.data.room.dao.VegetableDetailDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) = Room
        .databaseBuilder(context, VegetableDatabase::class.java, "vegetable_database")
        .addMigrations(MIGRATION_1_2)
        .build()

    @Provides
    fun provideVegetableDao(database: VegetableDatabase): VegetableDao = database.vegetableDao()

    @Provides
    fun provideVegetableDetailDao(database: VegetableDatabase): VegetableDetailDao = database.vegetableDetailDao()
}
