package com.moritoui.vegegrowthapp.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.moritoui.vegegrowthapp.data.room.model.VegetableDetailEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VegetableDetailDao {
    /**
     * 登録する
     */
    @Upsert
    suspend fun upsertVegetableDetail(vegetableDetail: VegetableDetailEntity): Long

    /**
     * 保存されている野菜の最新情報を取得する
     */
    @Transaction
    @Query("SELECT * FROM vegetable_detail_resources WHERE vegetable_id = :id ORDER BY date DESC LIMIT 1")
    fun getVegetableDetailsLast(id: Int): Flow<VegetableDetailEntity?>
}
