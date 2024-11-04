package com.moritoui.vegegrowthapp.ui.timeline.home.model

import androidx.paging.PagingData
import com.moritoui.vegegrowthapp.model.VegeItem

data class TimelineHomeState(val vegetables: PagingData<VegeItem>) {
    companion object {
        fun initial(): TimelineHomeState = TimelineHomeState(
            vegetables = PagingData.empty()
        )
    }
}
