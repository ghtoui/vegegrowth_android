package com.moritoui.vegegrowthapp.usecases

import com.moritoui.vegegrowthapp.model.SortStatus
import com.moritoui.vegegrowthapp.repository.VegeItemListRepository
import javax.inject.Inject

class SetSelectSortStatusUseCase @Inject constructor(
    private val vegeItemListRepository: VegeItemListRepository
) {
    operator fun invoke(sortStatus: SortStatus) {
        vegeItemListRepository.setSelectedSortStatus(sortStatus = sortStatus)
    }
}
