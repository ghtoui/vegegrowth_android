package com.moritoui.vegegrowthapp.ui.home.model

import com.moritoui.vegegrowthapp.data.room.model.VegetableFolderEntity
import com.moritoui.vegegrowthapp.model.VegeItem
import com.moritoui.vegegrowthapp.model.VegeItemDetail

data class HomeVegetablesState(
    val vegetables: List<VegeItem>,
    val vegetableDetails: List<VegeItemDetail?>,
    val vegetableFolders: List<VegetableFolderEntity>,
) {
    companion object {
        fun initial() = HomeVegetablesState(
            vegetables = emptyList(),
            vegetableDetails = emptyList(),
            vegetableFolders = emptyList(),
        )
    }
}
