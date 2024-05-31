package com.moritoui.vegegrowthapp.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import com.moritoui.vegegrowthapp.data.room.model.VegetableDetailEntity

@Dao
interface VegetableDetailDao {
    /**
    * 登録する
    */
    @Insert
    suspend fun insertVegetableDetail(vegetableDetail: VegetableDetailEntity): Long
}
