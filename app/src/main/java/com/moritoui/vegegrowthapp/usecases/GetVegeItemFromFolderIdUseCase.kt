package com.moritoui.vegegrowthapp.usecases

import com.moritoui.vegegrowthapp.model.VegeItem
import com.moritoui.vegegrowthapp.repository.vegetable.VegetableRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetVegeItemFromFolderIdUseCase @Inject constructor(private val vegetableRepository: VegetableRepository) {
    operator fun invoke(folderId: Int?): Flow<List<VegeItem>> = vegetableRepository.getVegetablesFromFolderId(folderId)
}
