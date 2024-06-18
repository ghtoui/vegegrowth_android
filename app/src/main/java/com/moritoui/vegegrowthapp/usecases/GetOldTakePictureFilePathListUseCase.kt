package com.moritoui.vegegrowthapp.usecases

import com.moritoui.vegegrowthapp.repository.VegeItemDetailRepository
import javax.inject.Inject

class GetOldTakePictureFilePathListUseCase
@Inject
constructor(private val vegeItemDetailRepository: VegeItemDetailRepository) {
    operator fun invoke(): List<String> = vegeItemDetailRepository.takePictureFilePathList
}
