package com.moritoui.vegegrowthapp.usecases

import com.moritoui.vegegrowthapp.model.VegeItem
import com.moritoui.vegegrowthapp.repository.VegeItemListRepository
import javax.inject.Inject

class ChangeVegeItemStatusUseCase @Inject constructor(
    private val vegeItemListRepository: VegeItemListRepository
) {
    operator fun invoke (vegeItem: VegeItem) {
        vegeItemListRepository.changeVegeItemStatus(vegeItem)
    }
}
