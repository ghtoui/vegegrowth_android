package com.moritoui.vegegrowthapp.data.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.moritoui.vegegrowthapp.model.VegeCategory
import com.moritoui.vegegrowthapp.model.VegeItem
import com.moritoui.vegegrowthapp.model.VegeStatus

@Entity(tableName = "vegetable_resources")
data class VegetableEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "vegetable_name") val vegetableName: String,
    @ColumnInfo(name = "vegetable_category") val vegetableCategory: VegeCategory,
    @ColumnInfo(name = "vegetable_state") val vegetableState: VegeStatus,
    @ColumnInfo(name = "folder_id") val folderId: Int?,
)

fun VegetableEntity.toVegeItem(): VegeItem = VegeItem(
    name = vegetableName,
    category = vegetableCategory,
    id = id,
    status = vegetableState,
    folderId = folderId,
)
