package com.moritoui.vegegrowthapp.usecases

import android.graphics.Bitmap
import com.moritoui.vegegrowthapp.repository.VegeItemDetailRepository
import javax.inject.Inject

class GetTakePictureListUseCase @Inject constructor(
    private val vegeItemDetailRepository: VegeItemDetailRepository
) {
    operator fun invoke(): List<Bitmap?> {
        return vegeItemDetailRepository.takePictureList
    }
}
