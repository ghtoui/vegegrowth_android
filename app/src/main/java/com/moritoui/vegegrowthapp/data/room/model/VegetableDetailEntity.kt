package com.moritoui.vegegrowthapp.data.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.moritoui.vegegrowthapp.model.VegeItemDetail

@Entity(
    tableName = "vegetable_detail_resources",
    foreignKeys = [ForeignKey(
        entity = VegetableEntity::class,
        parentColumns = ["id"],
        childColumns = ["vegetable_detail_id"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["vegetable_detail_id"])]
)
data class VegetableDetailEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "vegetable_detail_id") val vegetableDetailId: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "image") val image: String,
    @ColumnInfo(name = "size") val size: Double,
    @ColumnInfo(name = "note") val note: String,
    @ColumnInfo(name = "date") val date: String
)

fun VegetableDetailEntity.toVegeItemDetail(): VegeItemDetail = VegeItemDetail(
    itemId = vegetableDetailId,
    id = id,
    size = size,
    memo = note,
    date = date,
    image = image,
    name = name,
)
