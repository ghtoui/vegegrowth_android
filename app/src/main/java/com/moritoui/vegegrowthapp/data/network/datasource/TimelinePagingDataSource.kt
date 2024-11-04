package com.moritoui.vegegrowthapp.data.network.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.moritoui.vegegrowthapp.model.VegeItem
import com.moritoui.vegegrowthapp.repository.timeline.TimelineRepository
import javax.inject.Inject

class TimelinePagingDataSource @Inject constructor(private val timelineRepository: TimelineRepository) : PagingSource<Int, VegeItem>() {
    override fun getRefreshKey(state: PagingState<Int, VegeItem>): Int? = state.anchorPosition

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, VegeItem> {
        val page = params.key ?: 0

        return timelineRepository.getVegetables(page = page)
            .fold(
                onSuccess = {
                    LoadResult.Page(
                        data = it.datas,
                        prevKey = if (page == 0) {
                            null
                        } else {
                            page.minus(1)
                        },
                        nextKey = if (it.datas.isEmpty()) {
                            null
                        } else {
                            page.plus(1)
                        }
                    )
                },
                onFailure = {
                    LoadResult.Error(it)
                }
            )
    }
}
