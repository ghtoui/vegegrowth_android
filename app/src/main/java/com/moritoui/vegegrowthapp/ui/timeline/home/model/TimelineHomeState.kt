package com.moritoui.vegegrowthapp.ui.timeline.home.model

import com.moritoui.vegegrowthapp.model.PagingListState
import com.moritoui.vegegrowthapp.model.VegeItem

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
