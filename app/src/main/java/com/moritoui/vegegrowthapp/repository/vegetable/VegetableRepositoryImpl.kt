package com.moritoui.vegegrowthapp.repository.vegetable

import com.moritoui.vegegrowthapp.data.room.dao.VegetableDao
import com.moritoui.vegegrowthapp.data.room.model.toVegeItem
import com.moritoui.vegegrowthapp.model.VegeItem
import com.moritoui.vegegrowthapp.model.toVegeTableEntity
import java.io.File
import java.nio.file.Files
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class VegetableRepositoryImpl @Inject constructor(private val vegetableDao: VegetableDao) : VegetableRepository {
    override suspend fun deleteVegeItem(vegeItem: VegeItem) {
        vegetableDao.getVegetableWithDetails(vegeItem.id).map { vegetableDetails ->
            vegetableDetails?.details?.map {
                try {
                    Files.delete(File(it.imagePath).toPath())
                } catch (_: Exception) { }
            }
        }
        vegetableDao.deleteVegetable(vegeItem.toVegeTableEntity())
    }

    override suspend fun addVegeItem(vegeItem: VegeItem) {
        vegetableDao.upsertVegetable(vegeItem.toVegeTableEntity())
    }

    override suspend fun changeVegeItemStatus(vegeItem: VegeItem) {
        vegetableDao.upsertVegetable(vegeItem.toVegeTableEntity())
    }

    override suspend fun getVegetables(): List<VegeItem> = vegetableDao.getVegetables().map {
        it.toVegeItem()
    }

    override fun getVegetablesFromFolderId(folderId: Int?): Flow<List<VegeItem>> = vegetableDao.getVegetableFromFolderId(folderId).map { vegetables ->
        vegetables.map { it.toVegeItem() }
    }
}
