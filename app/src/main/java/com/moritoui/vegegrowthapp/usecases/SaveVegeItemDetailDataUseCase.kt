package com.moritoui.vegegrowthapp.usecases

import android.graphics.Bitmap
import com.moritoui.vegegrowthapp.model.VegeItemDetail
import com.moritoui.vegegrowthapp.repository.VegeItemDetailRepository
import javax.inject.Inject

class SaveVegeItemDetailDataUseCase
@Inject
constructor(private val vegeItemDetailRepository: VegeItemDetailRepository) {
    operator fun invoke(takePicture: Bitmap?, vegeItemDetailList: MutableList<VegeItemDetail>) {
        vegeItemDetailRepository.saveVegeDetail(takePicture, vegeItemDetailList)
    }
}
