package com.moritoui.vegegrowthapp.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.moritoui.vegegrowthapp.data.room.model.VegetableFolderEntity
import kotlinx.coroutines.flow.Flow

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
    @Query("SELECT * FROM vegetable_folder_resources")
    fun getVegetableFolder(): Flow<List<VegetableFolderEntity>>

    /**
     * 選択したものを削除する
     */
    @Delete
    suspend fun deleteVegetableFolder(vegetableFolderEntity: VegetableFolderEntity)

    /**
     * 選択されたフォルダーを取得
     */
    @Query("SELECT * FROM vegetable_folder_resources WHERE id = :id")
    suspend fun getSelectedFolder(id: Int): VegetableFolderEntity
}
