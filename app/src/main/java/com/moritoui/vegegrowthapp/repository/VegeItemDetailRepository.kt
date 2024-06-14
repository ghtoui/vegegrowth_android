package com.moritoui.vegegrowthapp.repository

import android.graphics.Bitmap
import com.moritoui.vegegrowthapp.model.VegeItemDetail
import com.moritoui.vegegrowthapp.model.VegetableRepositoryFileManager
import javax.inject.Inject

interface VegeItemDetailRepository {
    val vegeItemDetailList: MutableList<VegeItemDetail>
    var takePictureFilePathList: List<String>

    fun saveVegeDetail(
        takePicture: Bitmap?,
        vegeItemDetailList: MutableList<VegeItemDetail>,
    )

    fun saveVegeDetailMemo(
        index: Int,
        memo: String,
    )
}

class VegeItemDetailRepositoryImpl
    @Inject
    constructor(
        private val fileManager: VegetableRepositoryFileManager,
    ) : VegeItemDetailRepository {
        override val vegeItemDetailList: MutableList<VegeItemDetail> = fileManager.getVegeRepositoryList()
        override var takePictureFilePathList: List<String> = fileManager.getImagePathList()

        override fun saveVegeDetail(
            takePicture: Bitmap?,
            vegeItemDetailList: MutableList<VegeItemDetail>,
        ) {
            fileManager.saveVegeRepositoryAndImage(
                vegeRepositoryList = vegeItemDetailList,
                takePicImage = takePicture,
            )
            takePictureFilePathList = fileManager.getImagePathList()
        }

        override fun saveVegeDetailMemo(
            index: Int,
            memo: String,
        ) {
            vegeItemDetailList[index].memo = memo
            fileManager.saveVegeRepository(vegeItemDetailList)
        }
    }
