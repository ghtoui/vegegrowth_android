package com.moritoui.vegegrowthapp.ui.folder.model

import com.moritoui.vegegrowthapp.model.VegeItem
import com.moritoui.vegegrowthapp.model.VegeItemDetail

data class VegetablesState(
    val vegetables: List<VegeItem>,
    val vegetableDetails: List<VegeItemDetail?>
) {
    companion object {
        fun initial(): VegetablesState = VegetablesState(
            vegetables = emptyList(),
            vegetableDetails = emptyList()
        )
    }
}
