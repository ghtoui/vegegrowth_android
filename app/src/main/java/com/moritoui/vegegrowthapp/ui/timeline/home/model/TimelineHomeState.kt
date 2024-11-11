package com.moritoui.vegegrowthapp.ui.timeline.home.model

import com.moritoui.vegegrowthapp.model.VegeItem
import com.moritoui.vegegrowthapp.ui.timeline.home.PagingListState

data class TimelineHomeState(
    val vegetables: List<VegeItem>,
    val pagingListState: PagingListState,
) {
    companion object {
        fun initial(): TimelineHomeState = TimelineHomeState(
            vegetables = emptyList(),
            pagingListState = PagingListState.Initial,
        )
    }
}
