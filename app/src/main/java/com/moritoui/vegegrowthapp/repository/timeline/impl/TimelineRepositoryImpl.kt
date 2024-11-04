package com.moritoui.vegegrowthapp.repository.timeline.impl

import com.moritoui.vegegrowthapp.data.network.VegetableApi
import com.moritoui.vegegrowthapp.model.VegeItemData
import com.moritoui.vegegrowthapp.repository.timeline.TimelineRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class TimelineRepositoryImpl @Inject constructor(
    private val vegetableApi: VegetableApi,
): TimelineRepository {
    override suspend fun getVegetables(page: Int): Result<VegeItemData> = runCatching {
        // TODO: ダミーの遅延
        delay(1000L)

        val vegetablesResult = vegetableApi.getVegetables(page = page).body()?.toDomain()

        vegetablesResult!!
    }
}
