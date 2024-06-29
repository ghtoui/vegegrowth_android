package com.moritoui.vegegrowthapp.usecases

import android.database.sqlite.SQLiteException
import com.moritoui.vegegrowthapp.data.room.dao.VegetableFolderDao
import com.moritoui.vegegrowthapp.data.room.model.VegetableFolderEntity
import javax.inject.Inject

class InsertVegetableFolderUseCase @Inject constructor(private val vegetableFolderDao: VegetableFolderDao) {
    suspend operator fun invoke(vegetableFolder: VegetableFolderEntity): Result<Unit> = try {
        Result.success(vegetableFolderDao.insertVegetableFolder(vegetableFolder))
    } catch (exception: SQLiteException) {
        Result.failure(exception)
    }
}
