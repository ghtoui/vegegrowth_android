package com.moritoui.vegegrowthapp.data.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.moritoui.vegegrowthapp.model.VegeCategory

@Entity(tableName = "vegetable_folder_resources", indices = [Index("folder_name", unique = true)])
data class VegetableFolderEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "folder_name") val folderName: String,
    @ColumnInfo(name = "folder_number") val folderNumber: Int,
    @ColumnInfo(name = "vegetable_category") val vegetableCategory: VegeCategory,
)
