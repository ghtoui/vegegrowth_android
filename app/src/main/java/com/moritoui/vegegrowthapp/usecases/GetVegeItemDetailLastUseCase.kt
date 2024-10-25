package com.moritoui.vegegrowthapp.usecases

import com.moritoui.vegegrowthapp.data.room.dao.VegetableDetailDao
import com.moritoui.vegegrowthapp.data.room.model.toVegeItemDetail
import com.moritoui.vegegrowthapp.model.VegeItemDetail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetVegeItemDetailLastUseCase @Inject constructor(private val vegetableDetailDao: VegetableDetailDao) {
    operator fun invoke(id: Int): Flow<VegeItemDetail?> = vegetableDetailDao.getVegetableDetailsLast(id).map { it?.toVegeItemDetail() }
}
