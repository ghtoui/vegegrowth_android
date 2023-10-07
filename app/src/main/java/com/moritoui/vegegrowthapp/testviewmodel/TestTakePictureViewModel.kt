package com.moritoui.vegegrowthapp.testviewmodel

import androidx.camera.core.ImageProxy
import com.moritoui.vegegrowthapp.di.TakePictureScreenUiState
import com.moritoui.vegegrowthapp.di.TakePictureViewModel
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
