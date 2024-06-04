package com.moritoui.vegegrowthapp.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.moritoui.vegegrowthapp.data.room.dao.VegetableDao
import com.moritoui.vegegrowthapp.data.room.dao.VegetableDetailDao
import com.moritoui.vegegrowthapp.data.room.model.VegetableDetailEntity
import com.moritoui.vegegrowthapp.data.room.model.VegetableEntity

@Database(
    entities = [
        VegetableEntity::class,
        VegetableDetailEntity::class
    ],
    version = 1,
    autoMigrations = [],
    exportSchema = true
)
abstract class VegetableDatabase : RoomDatabase() {
    abstract fun vegetableDao(): VegetableDao
    abstract fun vegetableDetailDao(): VegetableDetailDao
}
