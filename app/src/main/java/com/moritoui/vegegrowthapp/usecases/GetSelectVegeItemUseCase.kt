package com.moritoui.vegegrowthapp.usecases

import com.moritoui.vegegrowthapp.model.VegeItem
import com.moritoui.vegegrowthapp.repository.VegeItemListRepository
import javax.inject.Inject

class GetSelectVegeItemUseCase @Inject constructor(
    private val vegeItemListRepository: VegeItemListRepository
) {
    // nullの時はエラー処理をしたいが
    operator fun invoke(): VegeItem {
        val sortList = vegeItemListRepository.sortItemList()
        return sortList[vegeItemListRepository.selectIndex!!]
    }
}
