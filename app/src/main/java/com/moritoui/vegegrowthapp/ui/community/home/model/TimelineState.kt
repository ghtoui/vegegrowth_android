package com.moritoui.vegegrowthapp.ui.community.home.model

import com.moritoui.vegegrowthapp.model.VegeItem

data class TimelineState(
    val datas: List<VegeItem>,
    val isAutoAppendLoading: Boolean,
) {
    companion object {
        fun initial(): TimelineState = TimelineState(
            datas = emptyList(),
            isAutoAppendLoading = false,
        )
    }
}
