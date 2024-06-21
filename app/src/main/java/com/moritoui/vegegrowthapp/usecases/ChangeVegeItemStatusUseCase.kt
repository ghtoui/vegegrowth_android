package com.moritoui.vegegrowthapp.usecases

import com.moritoui.vegegrowthapp.model.VegeItem
import com.moritoui.vegegrowthapp.repository.vegetable.VegetableRepository
import javax.inject.Inject

class ChangeVegeItemStatusUseCase @Inject constructor(private val vegetableRepository: VegetableRepository) {
    suspend operator fun invoke(vegeItem: VegeItem) {
        vegetableRepository.changeVegeItemStatus(vegeItem)
    }
}
