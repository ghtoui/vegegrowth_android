package com.moritoui.vegegrowthapp.ui

import androidx.camera.core.ImageProxy
import kotlinx.coroutines.flow.StateFlow

class TestTakePictureViewModel : TakePictureViewModel {
    override val uiState: StateFlow<TakePictureScreenUiState>
        get() = uiState

    override fun openRegisterDialog() { }

    override fun closeRegisterDialog() { }

    override fun registerVegeData() { }

    override fun checkInputText(inputText: String): Boolean {
        return super.checkInputText(inputText)
    }
    override fun setImage(takePic: ImageProxy) { }

    override fun changeInputText(inputText: String) { }

    override fun getIndex(): Int {
        return 0
    }

    override fun changeCameraOpenState() { }
}
