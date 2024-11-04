package com.moritoui.vegegrowthapp.ui.community.home.model

import com.moritoui.vegegrowthapp.model.VegeItem

data class CommunityHomeState(
    val datas: List<VegeItem>,
    val isAutoAppendLoading: Boolean,
) {
    companion object {
        fun initial(): CommunityHomeState = CommunityHomeState(
            datas = emptyList(),
            isAutoAppendLoading = false,
        )
    }
}
