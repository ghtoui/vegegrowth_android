package com.moritoui.vegegrowthapp.ui.main.model

data class MainState(
    val isInitialStartApp: Boolean,
    val isLoading: Boolean,
) {
    companion object {
        fun initial(): MainState = MainState(
            isInitialStartApp = true,
            isLoading = true
        )
    }
}
