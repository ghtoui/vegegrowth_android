package com.moritoui.vegegrowthapp.usecases

import com.moritoui.vegegrowthapp.repository.vegetabledetail.VegetableDetailRepository
import javax.inject.Inject

class DeleteImageUseCase @Inject constructor(
    private val vegetableDetailRepository: VegetableDetailRepository
) {
    operator fun invoke(imagePath: String): Result<Unit> = vegetableDetailRepository.deleteImage(imagePath)
}
