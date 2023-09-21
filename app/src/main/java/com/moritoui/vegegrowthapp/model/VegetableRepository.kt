package com.moritoui.vegegrowthapp.model

import android.graphics.Bitmap
import java.util.UUID

data class VegetableRepository(
    val uuid: UUID,
    val name: String,
    val size: Double,
    var memo: String,
    val picture: Bitmap
)