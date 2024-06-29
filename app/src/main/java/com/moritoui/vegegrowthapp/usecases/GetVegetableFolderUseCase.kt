package com.moritoui.vegegrowthapp.usecases

import com.moritoui.vegegrowthapp.data.room.dao.VegetableFolderDao
import com.moritoui.vegegrowthapp.data.room.model.VegetableFolderEntity
import javax.inject.Inject

class GetVegetableFolderUseCase @Inject constructor(
    private val dao: VegetableFolderDao
) {
    suspend operator fun invoke(): List<VegetableFolderEntity> = dao.getVegetableFolder()
}
