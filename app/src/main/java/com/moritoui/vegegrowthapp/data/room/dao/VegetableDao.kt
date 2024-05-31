package com.moritoui.vegegrowthapp.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.moritoui.vegegrowthapp.data.room.model.VegetableEntity
import com.moritoui.vegegrowthapp.data.room.model.VegetableWIthDetails

/**
 * [Vegetable] に関するDao
 */
@Dao
interface VegetableDao {
    /**
     * 登録する
     */
    @Insert
    suspend fun insertVegetable(vegetable: VegetableEntity)

    /**
     * 保存されている全ての野菜を取得する
     */
    @Query("SELECT * FROM vegetable_resources")
    suspend fun getVegetables(): List<VegetableEntity>

    /**
     * 選択された野菜を削除する
     * 野菜の情報にCASCADEを設定しているため，野菜を削除するとその情報も削除される
     */
    @Delete
    suspend fun deleteVegetable(vegetable: VegetableEntity)

    /**
     * 選択された野菜の情報を取得する
     */
    @Transaction
    @Query("SELECT * FROM vegetable_resources WHERE id = :id")
    suspend fun getVegetableWithDetails(id: Int): VegetableWIthDetails?
}
