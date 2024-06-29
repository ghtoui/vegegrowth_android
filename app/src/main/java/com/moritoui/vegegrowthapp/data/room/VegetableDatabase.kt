package com.moritoui.vegegrowthapp.data.room

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.moritoui.vegegrowthapp.data.room.dao.VegetableDao
import com.moritoui.vegegrowthapp.data.room.dao.VegetableDetailDao
import com.moritoui.vegegrowthapp.data.room.dao.VegetableFolderDao
import com.moritoui.vegegrowthapp.data.room.model.VegetableDetailEntity
import com.moritoui.vegegrowthapp.data.room.model.VegetableEntity
import com.moritoui.vegegrowthapp.data.room.model.VegetableFolderEntity

@Database(
    entities = [
        VegetableEntity::class,
        VegetableDetailEntity::class,
        VegetableFolderEntity::class,
    ],
    version = 3,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 2, to = 3)
    ]
)
abstract class VegetableDatabase : RoomDatabase() {
    abstract fun vegetableDao(): VegetableDao

    abstract fun vegetableDetailDao(): VegetableDetailDao

    abstract fun vegetableFolderDao(): VegetableFolderDao
}

/**
 * 1から2へ移行
 * フォルダー分けするテーブルを作成して，登録した野菜にフォルダー番号を追加した．
 * フォルダの名前にindexを付与して重複を防ぐ
 */
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS vegetable_folder_resources (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    "folder_name TEXT NOT NULL," +
                    "folder_number INTEGER NOT NULL," +
                    "vegetable_category TEXT NOT NULL" +
                    ");"
        )
        db.execSQL(
            "CREATE UNIQUE INDEX IF NOT EXISTS `index_vegetable_folder_resources_folder_name` ON `vegetable_folder_resources` (`folder_name`)"
        )
        db.execSQL(
            "ALTER TABLE vegetable_resources ADD COLUMN folder_id INTEGER"
        )
    }
}
