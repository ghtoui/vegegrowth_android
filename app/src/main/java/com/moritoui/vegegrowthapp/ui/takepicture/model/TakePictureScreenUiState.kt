package com.moritoui.vegegrowthapp.ui.takepicture.model

import android.graphics.Bitmap

data class TakePictureScreenUiState(
    val vegeName: String,
    val isOpenDialog: Boolean,
    val inputText: String,
    val isSuccessInputText: Boolean,
    val isBeforeInputText: Boolean,
    val takePicImage: Bitmap?,
    val isVisibleNavigateButton: Boolean,
    val isCameraOpen: Boolean,
    val lastSavedSize: Double?,
    val isLoading: Boolean,
) {
    companion object {
        fun initialState(): TakePictureScreenUiState =
            TakePictureScreenUiState(
                vegeName = "",
                isOpenDialog = false,
                inputText = "",
                isSuccessInputText = false,
                isBeforeInputText = true,
                takePicImage = null,
                isVisibleNavigateButton = false,
                isCameraOpen = false,
                lastSavedSize = null,
                isLoading = false,
            )
    }
}
