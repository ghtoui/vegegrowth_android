package com.moritoui.vegegrowthapp.data.room.model

import androidx.room.Embedded
import androidx.room.Relation

/**
 * VegetableとVegetableDetailの情報を定義
 */
data class VegetableWIthDetails (
    @Embedded val vegetable: VegetableEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "vegetable_detail_id"
    ) val details: List<VegetableDetailEntity>
)
