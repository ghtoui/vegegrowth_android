package com.moritoui.vegegrowthapp.repository.vegetabledetail

import android.graphics.Bitmap
import com.moritoui.vegegrowthapp.model.VegeItemDetail

interface VegetableDetailRepository {
    /**
     * 野菜の大きさと画像の登録
     */
    suspend fun addVegeItemDetail(vegeItemDetail: VegeItemDetail)

    /**
     * メモの編集
     */
    suspend fun editMemo(
        memo: String,
        vegeItemDetail: VegeItemDetail,
    )

    /**
     * 画像の保存
     */
    fun saveTookPicture(tookPicture: Bitmap): String
}
