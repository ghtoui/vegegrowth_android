package com.moritoui.vegegrowthapp.repository.timeline

import com.moritoui.vegegrowthapp.model.VegeItemData

/**
 * タイムラインに関するリポジトリ
 */
interface TimelineRepository {
    /**
     * @param page: 読み込むページ
     */
    suspend fun getVegetables(
        page: Int
    ): Result<VegeItemData>
}
