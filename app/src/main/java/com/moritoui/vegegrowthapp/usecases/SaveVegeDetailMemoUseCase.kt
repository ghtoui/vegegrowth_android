package com.moritoui.vegegrowthapp.usecases

import com.moritoui.vegegrowthapp.model.VegeItemDetail
import com.moritoui.vegegrowthapp.repository.vegetabledetail.VegetableDetailRepository
import javax.inject.Inject

class SaveVegeDetailMemoUseCase
    @Inject
    constructor(
        private val vegetableDetailRepository: VegetableDetailRepository,
    ) {
        suspend operator fun invoke(
            memo: String,
            vegeItemDetail: VegeItemDetail,
        ) {
            vegetableDetailRepository.editMemo(memo, vegeItemDetail)
        }
    }
