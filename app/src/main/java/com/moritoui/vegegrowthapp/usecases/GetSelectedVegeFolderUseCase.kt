package com.moritoui.vegegrowthapp.usecases

import com.moritoui.vegegrowthapp.data.room.dao.VegetableFolderDao
import com.moritoui.vegegrowthapp.data.room.model.VegetableFolderEntity
import javax.inject.Inject

class GetSelectedVegeFolderUseCase @Inject constructor(
    private val dao: VegetableFolderDao
) {
    suspend operator fun invoke(folderId: Int): VegetableFolderEntity = dao.getSelectedFolder(folderId)
}
