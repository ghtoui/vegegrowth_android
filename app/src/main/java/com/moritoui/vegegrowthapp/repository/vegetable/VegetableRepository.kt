package com.moritoui.vegegrowthapp.repository.vegetable

import com.moritoui.vegegrowthapp.model.VegeItem
import kotlinx.coroutines.flow.Flow

interface VegetableRepository {
    /**
     * 野菜の削除
     * それに関わる情報を削除される(画像も全て)
     */
    suspend fun deleteVegeItem(vegeItem: VegeItem)

    /**
     * 管理する野菜の追加
     */
    suspend fun addVegeItem(vegeItem: VegeItem)

    /**
     * 野菜のステータス変更
     */
    suspend fun changeVegeItemStatus(vegeItem: VegeItem)

    /**
     * 保存されている全ての野菜を取得
     */
    suspend fun getVegetables(): List<VegeItem>

    /**
     * フォルダIDが一致する野菜を取得する
     */
    fun getVegetablesFromFolderId(folderId: Int?): Flow<List<VegeItem>>
}
