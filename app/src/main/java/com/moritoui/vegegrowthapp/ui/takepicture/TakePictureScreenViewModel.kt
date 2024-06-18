package com.moritoui.vegegrowthapp.ui.takepicture

import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Bundle
import androidx.camera.core.ImageProxy
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.ktx.Firebase
import com.moritoui.vegegrowthapp.model.DateFormatter
import com.moritoui.vegegrowthapp.model.VegeItemDetail
import com.moritoui.vegegrowthapp.repository.vegetabledetail.VegetableDetailRepository
import com.moritoui.vegegrowthapp.ui.takepicture.model.TakePictureScreenUiState
import com.moritoui.vegegrowthapp.usecases.GetSelectedVegeItemUseCase
import com.moritoui.vegegrowthapp.usecases.GetVegetableDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDateTime
import java.util.UUID
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TakePictureScreenViewModel
    @Inject
    constructor(
        private val dateFormatter: DateFormatter,
        private val vegetableDetailRepository: VegetableDetailRepository,
        private val getVegetableDetailsUseCase: GetVegetableDetailsUseCase,
        private val getSelectedVegeItemUseCase: GetSelectedVegeItemUseCase,
        savedStateHandle: SavedStateHandle,
    ) : ViewModel() {
        val args = checkNotNull(savedStateHandle.get<Int>("vegetableId"))

        private val firebaseAnalytics = Firebase.analytics
        private val selectedVegeItem = viewModelScope.async { getSelectedVegeItemUseCase(args) }

        private val _uiState = MutableStateFlow(TakePictureScreenUiState.initialState())
        val uiState: StateFlow<TakePictureScreenUiState> = _uiState.asStateFlow()

        init {
            _uiState.onEach {
                _uiState.update {
                    it.copy(
                        isLoading = true,
                    )
                }
                val vegetableDetails = getVegetableDetailsUseCase(args)
                _uiState.update {
                    it.copy(
                        isVisibleNavigateButton = vegetableDetails.isNotEmpty(),
                        lastSavedSize = vegetableDetails.lastOrNull()?.size,
                        isLoading = false,
                    )
                }
            }.launchIn(viewModelScope)
        }

        private fun updateState(
            isOpenDialog: Boolean = _uiState.value.isOpenDialog,
            inputText: String = _uiState.value.inputText,
            isSuccessInputText: Boolean = _uiState.value.isSuccessInputText,
            isBeforeInputText: Boolean = _uiState.value.isBeforeInputText,
            takePicImage: Bitmap? = _uiState.value.takePicImage,
            isVisibleNavigateButton: Boolean = _uiState.value.isVisibleNavigateButton,
            isCameraOpen: Boolean = _uiState.value.isCameraOpen,
        ) {
            _uiState.update { currentState ->
                currentState.copy(
                    isOpenDialog = isOpenDialog,
                    inputText = inputText,
                    isSuccessInputText = isSuccessInputText,
                    isBeforeInputText = isBeforeInputText,
                    takePicImage = takePicImage,
                    isVisibleNavigateButton = isVisibleNavigateButton,
                    isCameraOpen = isCameraOpen,
                )
            }
        }

        private fun resetState() {
            updateState(
                isOpenDialog = false,
                inputText = "",
                isSuccessInputText = false,
                isBeforeInputText = true,
                takePicImage = null,
            )
        }

        fun openRegisterDialog() {
            updateState(isOpenDialog = true)
        }

        fun closeRegisterDialog() {
            updateState(
                isOpenDialog = false,
                inputText = "",
                isBeforeInputText = true,
            )
        }

        fun registerVegeData() {
            val datetime = dateFormatter.dateToString(LocalDateTime.now())
            // UIの部分で画像が撮影されていないとこのボタンを押せないため，nullでくることはないはず
            val takePicImage = _uiState.value.takePicImage ?: return
            val imagePath = vegetableDetailRepository.saveTookPicture(takePicImage)

            viewModelScope.launch {
                val selectedVegeItem = selectedVegeItem.await()
                val registerVegeItemDetail =
                    VegeItemDetail(
                        vegeItemId = selectedVegeItem.id,
                        uuid = UUID.randomUUID().toString(),
                        name = selectedVegeItem.name,
                        size = _uiState.value.inputText.toDouble(),
                        memo = "",
                        date = datetime,
                        imagePath = imagePath,
                    )
                vegetableDetailRepository.addVegeItemDetail(registerVegeItemDetail)
            }
            resetState()
            firebaseEventSend()
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
                isBeforeInputText = false,
            )
        }

        private fun checkInputText(inputText: String): Boolean =
            when (inputText.toDoubleOrNull()) {
                null -> false
                else -> true
            }

        private fun fixRotateImage(takePic: ImageProxy): Bitmap {
            val rotation = takePic.imageInfo.rotationDegrees
            val takePicBitMap = takePic.toBitmap()

            val matrix = Matrix()
            matrix.postRotate(rotation.toFloat())
            return Bitmap.createBitmap(takePicBitMap, 0, 0, takePic.width, takePic.height, matrix, true)
        }

        fun changeCameraOpenState() {
            _uiState.update {
                it.copy(
                    isCameraOpen = !it.isCameraOpen,
                )
            }
        }

        private fun firebaseEventSend() {
            // ユーザが野菜の情報を登録したときのログを取る
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM) {
                Bundle().apply {
                    param(FirebaseAnalytics.Param.ITEM_ID, "register_detail")
                    param(FirebaseAnalytics.Param.ITEM_NAME, "registerDetail")
                    param(FirebaseAnalytics.Param.CONTENT_TYPE, "data")
                }
            }
        }
    }
