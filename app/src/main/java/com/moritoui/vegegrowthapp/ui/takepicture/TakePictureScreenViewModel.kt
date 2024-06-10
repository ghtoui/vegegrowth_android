package com.moritoui.vegegrowthapp.ui.takepicture

import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.camera.core.ImageProxy
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moritoui.vegegrowthapp.di.TakePictureScreenUiState
import com.moritoui.vegegrowthapp.model.DateFormatter
import com.moritoui.vegegrowthapp.model.VegeItem
import com.moritoui.vegegrowthapp.model.VegeItemDetail
import com.moritoui.vegegrowthapp.repository.vegetabledetail.VegetableDetailRepository
import com.moritoui.vegegrowthapp.usecases.GetSelectedVegeItemUseCase
import com.moritoui.vegegrowthapp.usecases.GetVegetableDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDateTime
import java.util.UUID
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TakePictureScreenViewModel @Inject constructor(
    private val dateFormatter: DateFormatter,
    private val vegetableDetailRepository: VegetableDetailRepository,
    private val getVegetableDetailsUseCase: GetVegetableDetailsUseCase,
    private val getSelectedVegeItemUseCase: GetSelectedVegeItemUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val args = checkNotNull(savedStateHandle.get<Int>("vegetableId"))

    private lateinit var selectedVegeItem: VegeItem

    private val _uiState = MutableStateFlow(TakePictureScreenUiState())
    val uiState: StateFlow<TakePictureScreenUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            selectedVegeItem = getSelectedVegeItemUseCase(args)
            _uiState.update {
                it.copy(
                    isVisibleNavigateButton = getVegetableDetailsUseCase(args).isNotEmpty()
                )
            }
        }
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
        // UIの部分で画像が撮影されていないとこのボタンを押せないため，nullでくることはないはず
        val takePicImage = _uiState.value.takePicImage ?: return
        val imagePath = vegetableDetailRepository.saveTookPicture(takePicImage)

        val registerVegeItemDetail = VegeItemDetail(
            vegeItemId = selectedVegeItem.id,
            uuid = UUID.randomUUID().toString(),
            name = selectedVegeItem.name,
            size = _uiState.value.inputText.toDouble(),
            memo = "",
            date = datetime,
            imagePath = imagePath
        )
        viewModelScope.launch {
            vegetableDetailRepository.addVegeItemDetail(registerVegeItemDetail)
        }
        resetState()
    }

    fun onTakePicture(takePicture: ImageProxy?) {
        takePicture ?: return
        val rotateTakePicture = fixRotateImage(takePic = takePicture)
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
