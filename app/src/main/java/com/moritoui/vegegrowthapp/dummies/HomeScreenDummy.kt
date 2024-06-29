package com.moritoui.vegegrowthapp.dummies

import com.moritoui.vegegrowthapp.data.room.model.VegetableFolderEntity
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
            folderId = null
        ),
        VegeItem(
            id = 1,
            name = "v2",
            VegeCategory.Flower,
            uuid = "",
            VegeStatus.Favorite,
            folderId = null
        ),
        VegeItem(
            id = 2,
            name = "v3",
            VegeCategory.Other,
            uuid = "",
            VegeStatus.Default,
            folderId = null
        ),
        VegeItem(
            id = 3,
            name = "v4",
            VegeCategory.None,
            uuid = "",
            VegeStatus.End,
            folderId = null
        )
    )
    fun vegeFolderList(): List<VegetableFolderEntity> = listOf(
        VegetableFolderEntity(
            id = 0,
            folderNumber = 0,
            folderName = "test0",
            vegetableCategory = VegeCategory.None
        ),
        VegetableFolderEntity(
            id = 1,
            folderNumber = 1,
            folderName = "test1",
            vegetableCategory = VegeCategory.Leaf
        ),
        VegetableFolderEntity(
            id = 2,
            folderNumber = 2,
            folderName = "test2",
            vegetableCategory = VegeCategory.Flower
        ),
        VegetableFolderEntity(
            id = 3,
            folderNumber = 3,
            folderName = "test3",
            vegetableCategory = VegeCategory.None
        )
    )
}
