package com.moritoui.vegegrowthapp.usecases

import com.moritoui.vegegrowthapp.data.room.dao.VegetableDao
import javax.inject.Inject

class GetImagePathListUseCase @Inject constructor(
    private val vegetableDao: VegetableDao
) {
    suspend operator fun invoke(vegetableId: Int): List<String> = vegetableDao.getVegetableWithDetails(vegetableId)?.details?.map {
        it.imagePath
    } ?: emptyList()
}
