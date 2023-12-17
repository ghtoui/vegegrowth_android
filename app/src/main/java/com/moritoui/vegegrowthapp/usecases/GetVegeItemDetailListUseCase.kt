package com.moritoui.vegegrowthapp.usecases

import com.moritoui.vegegrowthapp.model.VegeItemDetail
import com.moritoui.vegegrowthapp.repository.VegeItemDetailRepository
import javax.inject.Inject

class GetVegeItemDetailListUseCase @Inject constructor(
    private val vegeItemDetailRepository: VegeItemDetailRepository
) {
    operator fun invoke(): MutableList<VegeItemDetail> {
        return vegeItemDetailRepository.vegeItemDetailList
    }
}
