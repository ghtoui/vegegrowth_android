package com.moritoui.vegegrowthapp.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.moritoui.vegegrowthapp.data.room.dao.VegetableDao
import com.moritoui.vegegrowthapp.data.room.dao.VegetableDetailDao
import com.moritoui.vegegrowthapp.data.room.model.VegetableDetailEntity
import com.moritoui.vegegrowthapp.data.room.model.VegetableEntity
import com.moritoui.vegegrowthapp.data.room.model.VegetableFolderEntity

@Database(
    entities = [
        VegetableEntity::class,
        VegetableDetailEntity::class,
        VegetableFolderEntity::class,
    ],
    version = 2,
    exportSchema = true,
)
abstract class VegetableDatabase : RoomDatabase() {
    abstract fun vegetableDao(): VegetableDao

    abstract fun vegetableDetailDao(): VegetableDetailDao
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE vegetable_folder_resources (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    "folder_name TEXT NOT NULL," +
                    "folder_number INTEGER NOT NULL," +
                    "vegetable_category TEXT NOT NULL" +
                    ");"
        )
        db.execSQL(
            "ALTER TABLE vegetable_resources ADD COLUMN folder_id INTEGER"
        )
    }
}
