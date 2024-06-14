package com.moritoui.vegegrowthapp.repository.vegetable

import com.moritoui.vegegrowthapp.model.VegeItem

interface VegetableRepository {
    /**
     * 野菜の削除
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
}
