package com.moritoui.vegegrowthapp.usecases

import com.moritoui.vegegrowthapp.data.room.dao.VegetableFolderDao
import com.moritoui.vegegrowthapp.data.room.model.VegetableFolderEntity
import javax.inject.Inject

class DeleteVegeFolderUseCase @Inject constructor(private val dao: VegetableFolderDao) {
    suspend operator fun invoke(vegetableFolderEntity: VegetableFolderEntity) {
        dao.deleteVegetableFolder(vegetableFolderEntity)
    }
}
