package com.moritoui.vegegrowthapp.usecases

import com.moritoui.vegegrowthapp.repository.VegeItemListRepository
import javax.inject.Inject

class SetSelectedIndexUseCase
    @Inject
    constructor(
        private val vegeItemListRepository: VegeItemListRepository,
    ) {
        operator fun invoke(index: Int) {
            vegeItemListRepository.setSelectedIndex(index)
        }
    }
