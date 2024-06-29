package com.moritoui.vegegrowthapp.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.moritoui.vegegrowthapp.data.room.model.VegetableFolderEntity

/**
 * [VegetableFolderEntity]に関するDao
 */
@Dao
interface VegetableFolderDao {
    /**
     * 登録する.
     * 名前の重複を防ぐ
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertVegetableFolder(vegetableFolderEntity: VegetableFolderEntity)

    /**
     * アップデートする
     */
    @Update
    suspend fun updateVegetableFolder(vegetableFolderEntity: VegetableFolderEntity)

    /**
     * 保存されているフォルダーを取得する
     */
    @Transaction
    @Query("SELECT * FROM vegetable_folder_resources")
    suspend fun getVegetableFolder(): List<VegetableFolderEntity>
}
