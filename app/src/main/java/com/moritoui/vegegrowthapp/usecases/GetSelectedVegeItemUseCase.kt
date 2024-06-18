package com.moritoui.vegegrowthapp.usecases

import com.moritoui.vegegrowthapp.data.room.dao.VegetableDao
import com.moritoui.vegegrowthapp.data.room.model.toVegeItem
import com.moritoui.vegegrowthapp.model.VegeItem
import javax.inject.Inject

class GetSelectedVegeItemUseCase
@Inject
constructor(private val vegetableDao: VegetableDao) {
    suspend operator fun invoke(vegetableId: Int): VegeItem = vegetableDao.getSelectedVegetable(vegetableId).toVegeItem()
}
