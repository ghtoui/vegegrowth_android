package com.moritoui.vegegrowthapp.usecases

import com.moritoui.vegegrowthapp.data.room.dao.VegetableDetailDao
import com.moritoui.vegegrowthapp.data.room.model.VegetableDetailEntity
import java.io.IOException
import javax.inject.Inject

class DeleteVegeItemDetailUseCase @Inject constructor(private val vegetableDetailDao: VegetableDetailDao, private val deleteImageUseCase: DeleteImageUseCase) {
    suspend operator fun invoke(vegetableDetailEntity: VegetableDetailEntity): Result<Unit> {
        try {
            deleteImageUseCase(vegetableDetailEntity.imagePath).onSuccess {
                vegetableDetailDao.deleteVegetableDetails(vegetableDetailEntity)
            }
            return Result.success(Unit)
        } catch (e: IOException) {
            return Result.failure(e)
        }
    }
}
