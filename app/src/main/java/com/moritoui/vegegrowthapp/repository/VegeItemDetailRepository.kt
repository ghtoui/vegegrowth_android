package com.moritoui.vegegrowthapp.repository

import android.graphics.Bitmap
import com.moritoui.vegegrowthapp.model.VegeItemDetail
import com.moritoui.vegegrowthapp.model.VegetableRepositoryFileManager
import javax.inject.Inject

interface VegeItemDetailRepository {
    val vegeItemDetailList: MutableList<VegeItemDetail>
    val takePictureList: List<Bitmap?>
    fun saveVegeDetail(takePicture: Bitmap?)
    fun saveVegeDetailMemo(index: Int, memo: String)
}

class VegeItemDetailRepositoryImpl @Inject constructor(
    private val fileManager: VegetableRepositoryFileManager
) : VegeItemDetailRepository {
    override val vegeItemDetailList: MutableList<VegeItemDetail> = fileManager.getVegeRepositoryList()
    override val takePictureList: List<Bitmap?> = fileManager.getImageList()

    override fun saveVegeDetail(takePicture: Bitmap?) {
        fileManager.saveVegeRepositoryAndImage(
            vegeRepositoryList = vegeItemDetailList,
            takePicImage = takePicture
        )
    }

    override fun saveVegeDetailMemo(index: Int, memo: String) {
        vegeItemDetailList[index].memo = memo
        fileManager.saveVegeRepository(vegeItemDetailList)
    }
}
