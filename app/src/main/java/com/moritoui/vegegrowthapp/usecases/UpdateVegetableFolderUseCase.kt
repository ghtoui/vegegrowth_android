package com.moritoui.vegegrowthapp.usecases

import com.moritoui.vegegrowthapp.data.room.dao.VegetableFolderDao
import com.moritoui.vegegrowthapp.data.room.model.VegetableFolderEntity
import javax.inject.Inject

class UpdateVegetableFolderUseCase @Inject constructor(
    private val dao: VegetableFolderDao
) {
    suspend operator fun invoke(vegetableFolder: VegetableFolderEntity) {
        dao.updateVegetableFolder(vegetableFolder)
    }
}
