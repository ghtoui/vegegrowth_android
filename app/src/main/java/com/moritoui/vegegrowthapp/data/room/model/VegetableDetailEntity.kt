package com.moritoui.vegegrowthapp.data.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.moritoui.vegegrowthapp.model.VegeItemDetail

@Entity(
    tableName = "vegetable_detail_resources",
    foreignKeys = [
        ForeignKey(
            entity = VegetableEntity::class,
            parentColumns = ["id"],
            childColumns = ["vegetable_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["vegetable_id"])]
)
data class VegetableDetailEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "vegetable_id") val vegetableId: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "imagePath") val imagePath: String,
    @ColumnInfo(name = "size") val size: Double,
    @ColumnInfo(name = "note") val note: String,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "uuid") val uuid: String
)

fun VegetableDetailEntity.toVegeItemDetail(): VegeItemDetail = VegeItemDetail(
    vegeItemId = vegetableId,
    id = id,
    size = size,
    memo = note,
    date = date,
    imagePath = imagePath,
    name = name,
    uuid = uuid
)
