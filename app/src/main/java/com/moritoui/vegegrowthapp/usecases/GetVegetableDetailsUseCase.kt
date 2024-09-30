package com.moritoui.vegegrowthapp.usecases

import com.moritoui.vegegrowthapp.data.room.dao.VegetableDao
import com.moritoui.vegegrowthapp.data.room.model.toVegeItemDetail
import com.moritoui.vegegrowthapp.model.VegeItemDetail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * 詳細情報のリストを取得する
 *
 * 日付の順番で取得する
 */
class GetVegetableDetailsUseCase
@Inject
constructor(private val vegetableDao: VegetableDao) {
    suspend operator fun invoke(vegetableId: Int): Flow<List<VegeItemDetail>> {
        return vegetableDao.getVegetableWithDetails(vegetableId).map { vegetableDetails ->
            vegetableDetails?.details?.map {
                it.toVegeItemDetail()
            }?.sortedBy { it.date } ?: emptyList()
        }
    }
}
