package com.moritoui.vegegrowthapp.di

import android.graphics.Bitmap
import androidx.camera.core.ImageProxy
import kotlinx.coroutines.flow.StateFlow

data class TakePictureScreenUiState(
    val vegeName: String = "",
    val isOpenDialog: Boolean = false,
    val inputText: String = "",
    val isSuccessInputText: Boolean = false,
    val isBeforeInputText: Boolean = true,
    val takePicImage: Bitmap? = null,
    val isVisibleNavigateButton: Boolean = false,
    val isCameraOpen: Boolean = false
)
interface TakePictureViewModel {
    val uiState: StateFlow<TakePictureScreenUiState>

    fun openRegisterDialog()
    fun closeRegisterDialog()
    fun registerVegeData()
    fun setImage(takePic: ImageProxy)
    fun checkInputText(inputText: String): Boolean {
        return when (inputText.toDoubleOrNull()) {
            null -> false
            else -> true
        }
    }

    fun changeInputText(inputText: String)

    fun getIndex(): Int
    fun changeCameraOpenState()
}
