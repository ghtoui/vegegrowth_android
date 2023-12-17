package com.moritoui.vegegrowthapp.ui

import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.camera.core.ImageProxy
import androidx.lifecycle.ViewModel
import com.moritoui.vegegrowthapp.di.TakePictureScreenUiState
import com.moritoui.vegegrowthapp.model.DateFormatter
import com.moritoui.vegegrowthapp.model.VegeItem
import com.moritoui.vegegrowthapp.model.VegeItemDetail
import com.moritoui.vegegrowthapp.usecases.GetSelectVegeItemUseCase
import com.moritoui.vegegrowthapp.usecases.GetVegeItemDetailListUseCase
import com.moritoui.vegegrowthapp.usecases.SaveVegeItemDetailDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDateTime
import java.util.UUID
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class TakePictureScreenViewModel @Inject constructor(
    private val dateFormatter: DateFormatter,
    getSelectVegeItemUseCase: GetSelectVegeItemUseCase,
    getVegeItemDetailListUseCase: GetVegeItemDetailListUseCase,
    private val saveVegeItemDetailDataUseCase: SaveVegeItemDetailDataUseCase
) : ViewModel() {

    private var vegeRepositoryList: MutableList<VegeItemDetail> = getVegeItemDetailListUseCase()
    private var vegeItem: VegeItem = getSelectVegeItemUseCase()

    private val _uiState = MutableStateFlow(TakePictureScreenUiState())
    val uiState: StateFlow<TakePictureScreenUiState> = _uiState.asStateFlow()

    init {
        _uiState.update { currentState ->
            currentState.copy(vegeName = this.vegeItem.name)
        }
        updateState(isVisibleNavigateButton = vegeRepositoryList.isNotEmpty())
    }

    private fun updateState(
        isOpenDialog: Boolean = _uiState.value.isOpenDialog,
        inputText: String = _uiState.value.inputText,
        isSuccessInputText: Boolean = _uiState.value.isSuccessInputText,
        isBeforeInputText: Boolean = _uiState.value.isBeforeInputText,
        takePicImage: Bitmap? = _uiState.value.takePicImage,
        isVisibleNavigateButton: Boolean = _uiState.value.isVisibleNavigateButton,
        isCameraOpen: Boolean = _uiState.value.isCameraOpen
    ) {
        _uiState.update { currentState ->
            currentState.copy(
                isOpenDialog = isOpenDialog,
                inputText = inputText,
                isSuccessInputText = isSuccessInputText,
                isBeforeInputText = isBeforeInputText,
                takePicImage = takePicImage,
                isVisibleNavigateButton = isVisibleNavigateButton,
                isCameraOpen = isCameraOpen
            )
        }
    }

    private fun resetState() {
        updateState(
            isOpenDialog = false,
            inputText = "",
            isSuccessInputText = false,
            isBeforeInputText = true,
            takePicImage = null
        )
    }

    fun openRegisterDialog() {
        updateState(isOpenDialog = true)
    }

    fun closeRegisterDialog() {
        updateState(
            isOpenDialog = false,
            inputText = "",
            isBeforeInputText = true
        )
    }

    fun registerVegeData() {
        val datetime = dateFormatter.dateToString(LocalDateTime.now())
        // ボタンが押せないようにしているから、inputTextとtakePicImageはnullにならないはず
        vegeRepositoryList.add(
            VegeItemDetail(
                itemUuid = vegeItem.uuid.toString(),
                uuid = UUID.randomUUID().toString(),
                name = vegeItem.name,
                size = _uiState.value.inputText.toDouble(),
                memo = "",
                date = datetime
            )
        )
        saveVegeItemDetailDataUseCase(takePicture = _uiState.value.takePicImage)
        resetState()
        updateState(isVisibleNavigateButton = vegeRepositoryList.isNotEmpty())
    }

    fun setImage(takePic: ImageProxy) {
        val rotateTakePicture = fixRotateImage(takePic = takePic)
        updateState(takePicImage = rotateTakePicture)
    }

    fun changeInputText(inputText: String) {
        val isSuccessInputText = checkInputText(inputText = inputText)
        updateState(
            inputText = inputText,
            isSuccessInputText = isSuccessInputText,
            isBeforeInputText = false
        )
    }

    private fun checkInputText(inputText: String): Boolean {
        return when (inputText.toDoubleOrNull()) {
            null -> false
            else -> true
        }
    }

    private fun fixRotateImage(takePic: ImageProxy): Bitmap {
        val rotation = takePic.imageInfo.rotationDegrees
        val takePicBitMap = takePic.toBitmap()

        val matrix = Matrix()
        matrix.postRotate(rotation.toFloat())
        return Bitmap.createBitmap(takePicBitMap, 0, 0, takePic.width, takePic.height, matrix, true)
    }

    fun changeCameraOpenState() {
        updateState(isCameraOpen = !_uiState.value.isCameraOpen)
    }
}
