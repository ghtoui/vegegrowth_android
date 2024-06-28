package com.moritoui.vegegrowthapp.ui.home.model

import com.moritoui.vegegrowthapp.model.VegeItem
import com.moritoui.vegegrowthapp.model.VegeItemDetail

data class HomeVegetablesState(val vegetables: List<VegeItem>, val vegetableDetails: List<VegeItemDetail?>) {
    companion object {
        fun initial() = HomeVegetablesState(
            vegetables = emptyList(),
            emptyList()
        )
    }
}
