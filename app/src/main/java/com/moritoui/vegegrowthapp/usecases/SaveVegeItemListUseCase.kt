package com.moritoui.vegegrowthapp.usecases

import com.moritoui.vegegrowthapp.repository.VegeItemListRepository
import javax.inject.Inject

class SaveVegeItemListUseCase @Inject constructor(
    private val vegeItemListRepository: VegeItemListRepository
) {
    operator fun invoke() {
        vegeItemListRepository.saveVegeItemList()
    }
}
