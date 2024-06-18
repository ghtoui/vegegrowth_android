package com.moritoui.vegegrowthapp.repository.vegetable

import com.moritoui.vegegrowthapp.data.room.dao.VegetableDao
import com.moritoui.vegegrowthapp.data.room.model.toVegeItem
import com.moritoui.vegegrowthapp.model.VegeItem
import com.moritoui.vegegrowthapp.model.toVegeTableEntity
import javax.inject.Inject

class VegetableRepositoryImpl
@Inject
constructor(private val vegetableDao: VegetableDao) : VegetableRepository {
    override suspend fun deleteVegeItem(vegeItem: VegeItem) {
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
}
