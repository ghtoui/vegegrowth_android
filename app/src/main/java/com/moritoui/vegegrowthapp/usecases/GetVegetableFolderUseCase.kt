package com.moritoui.vegegrowthapp.usecases

import com.moritoui.vegegrowthapp.data.room.dao.VegetableFolderDao
import com.moritoui.vegegrowthapp.data.room.model.VegetableFolderEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetVegetableFolderUseCase @Inject constructor(private val dao: VegetableFolderDao) {
    operator fun invoke(): Flow<List<VegetableFolderEntity>> = dao.getVegetableFolder()
}
