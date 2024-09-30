package com.moritoui.vegegrowthapp.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.moritoui.vegegrowthapp.data.room.model.VegetableEntity
import com.moritoui.vegegrowthapp.data.room.model.VegetableWIthDetails
import kotlinx.coroutines.flow.Flow

/**
 * [Vegetable] に関するDao
 */
@Dao
interface VegetableDao {
    /**
     * すでにあれば，更新. なければ登録
     */
    @Upsert
    suspend fun upsertVegetable(vegetable: VegetableEntity)

    /**
     * 保存されている全ての野菜を取得する
     */
    @Query("SELECT * FROM vegetable_resources")
    suspend fun getVegetables(): List<VegetableEntity>

    /**
     * 選択された野菜のみ取得する
     */
    @Query("SELECT * FROM vegetable_resources WHERE id = :id")
    suspend fun getSelectedVegetable(id: Int): VegetableEntity

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
    fun getVegetableWithDetails(id: Int): Flow<VegetableWIthDetails?>

    /**
     * フォルダIDが一致するものを取得する
     */
    @Transaction
    @Query("SELECT * FROM vegetable_resources WHERE folder_id IS :folderId")
    suspend fun getVegetableFromFolderId(folderId: Int?): List<VegetableEntity>
}
