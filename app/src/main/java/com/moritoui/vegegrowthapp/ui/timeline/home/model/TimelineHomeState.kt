package com.moritoui.vegegrowthapp.ui.timeline.home.model

import com.moritoui.vegegrowthapp.model.VegeItem

data class TimelineHomeState(val datas: List<VegeItem>, val isAutoAppendLoading: Boolean) {
    companion object {
        fun initial(): TimelineHomeState = TimelineHomeState(
            datas = emptyList(),
            isAutoAppendLoading = false
        )
    }
}
