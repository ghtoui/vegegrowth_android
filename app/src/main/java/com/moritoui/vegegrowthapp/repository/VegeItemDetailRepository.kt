package com.moritoui.vegegrowthapp.repository

import android.graphics.Bitmap
import com.moritoui.vegegrowthapp.model.VegeItemDetail
import com.moritoui.vegegrowthapp.model.VegetableRepositoryFileManager
import javax.inject.Inject

interface VegeItemDetailRepository {
    val vegeItemDetailList: MutableList<VegeItemDetail>
    fun saveVegeDetail(takePicture: Bitmap?)
}

class VegeItemDetailRepositoryImpl @Inject constructor(
    private val fileManager: VegetableRepositoryFileManager
) : VegeItemDetailRepository {
    override val vegeItemDetailList: MutableList<VegeItemDetail> = fileManager.getVegeRepositoryList()

    override fun saveVegeDetail(takePicture: Bitmap?) {
        fileManager.saveVegeRepositoryAndImage(
            vegeRepositoryList = vegeItemDetailList,
            takePicImage = takePicture
        )
    }
}
