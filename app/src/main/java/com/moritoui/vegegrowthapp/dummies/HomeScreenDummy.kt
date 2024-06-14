package com.moritoui.vegegrowthapp.dummies

import com.moritoui.vegegrowthapp.model.VegeCategory
import com.moritoui.vegegrowthapp.model.VegeItem
import com.moritoui.vegegrowthapp.model.VegeStatus

object HomeScreenDummy {
    fun vegeList(): List<VegeItem> = listOf(
        VegeItem(
            id = 0,
            name = "v1",
            VegeCategory.Leaf,
            uuid = "",
            VegeStatus.End,
        ),
        VegeItem(
            id = 1,
            name = "v2",
            VegeCategory.Flower,
            uuid = "",
            VegeStatus.Favorite,
        ),
        VegeItem(
            id = 2,
            name = "v3",
            VegeCategory.Other,
            uuid = "",
            VegeStatus.Default,
        ),
        VegeItem(
            id = 3,
            name = "v4",
            VegeCategory.None,
            uuid = "",
            VegeStatus.End,
        ),
    )
}
