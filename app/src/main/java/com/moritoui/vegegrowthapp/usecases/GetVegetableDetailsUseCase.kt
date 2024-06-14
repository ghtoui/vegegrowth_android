package com.moritoui.vegegrowthapp.usecases

import com.moritoui.vegegrowthapp.data.room.dao.VegetableDao
import com.moritoui.vegegrowthapp.data.room.model.toVegeItemDetail
import com.moritoui.vegegrowthapp.model.VegeItemDetail
import javax.inject.Inject

class GetVegetableDetailsUseCase
    @Inject
    constructor(
        private val vegetableDao: VegetableDao,
    ) {
        suspend operator fun invoke(vegetableId: Int): List<VegeItemDetail> =
            vegetableDao.getVegetableWithDetails(vegetableId)?.details?.map {
                it.toVegeItemDetail()
            } ?: emptyList()
    }
