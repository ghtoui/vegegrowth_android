package com.moritoui.vegegrowthapp.data.room.dao

import androidx.room.Dao
import androidx.room.Upsert
import com.moritoui.vegegrowthapp.data.room.model.VegetableDetailEntity

@Dao
interface VegetableDetailDao {
    /**
     * 登録する
     */
    @Upsert
    suspend fun upsertVegetableDetail(vegetableDetail: VegetableDetailEntity): Long
}
