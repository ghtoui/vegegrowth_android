package com.moritoui.vegegrowthapp.usecases

import com.moritoui.vegegrowthapp.data.room.dao.VegetableDetailDao
import com.moritoui.vegegrowthapp.data.room.model.toVegeItemDetail
import com.moritoui.vegegrowthapp.model.VegeItemDetail
import javax.inject.Inject

class GetVegeItemDetailLastUseCase @Inject constructor(
    private val vegetableDetailDao: VegetableDetailDao
) {
    suspend operator fun invoke(id: Int): VegeItemDetail? = vegetableDetailDao.getVegetableDetailsLast(id)?.toVegeItemDetail()
}
