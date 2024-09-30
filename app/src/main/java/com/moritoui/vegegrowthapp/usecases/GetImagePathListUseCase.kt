package com.moritoui.vegegrowthapp.usecases

import com.moritoui.vegegrowthapp.data.room.dao.VegetableDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetImagePathListUseCase
@Inject
constructor(private val vegetableDao: VegetableDao) {
    suspend operator fun invoke(vegetableId: Int): Flow<List<String>> = vegetableDao.getVegetableWithDetails(vegetableId).map { vegetableDetails ->
        vegetableDetails?.details?.map {
            it.imagePath
        } ?: emptyList()
    }
}
