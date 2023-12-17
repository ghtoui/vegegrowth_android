package com.moritoui.vegegrowthapp.usecases

import com.moritoui.vegegrowthapp.repository.VegeItemDetailRepository
import javax.inject.Inject

class SaveVegeDetailMemoUseCase @Inject constructor(
    private val vegeItemDetailRepository: VegeItemDetailRepository
) {
    operator fun invoke(index: Int, memo: String) {
        vegeItemDetailRepository.saveVegeDetailMemo(index = index, memo = memo)
    }
}
