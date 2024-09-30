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
import com.moritoui.vegegrowthapp.core.analytics.AnalyticsEvent
import com.moritoui.vegegrowthapp.core.analytics.AnalyticsHelper
import com.moritoui.vegegrowthapp.model.DateFormatter
import com.moritoui.vegegrowthapp.model.VegeItemDetail
import com.moritoui.vegegrowthapp.repository.vegetabledetail.VegetableDetailRepository
import com.moritoui.vegegrowthapp.ui.takepicture.model.TakePictureScreenUiState
import com.moritoui.vegegrowthapp.usecases.GetSelectedVegeItemUseCase
import com.moritoui.vegegrowthapp.usecases.GetVegetableDetailsUseCase
import com.moritoui.vegegrowthapp.usecases.IsRegisterSelectDateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.UUID
import kotlinx.coroutines.async
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
    private val isRegisterSelectDateUseCase: IsRegisterSelectDateUseCase,
    private val analytics: AnalyticsHelper,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val args = checkNotNull(savedStateHandle.get<Int>("vegetableId"))

    private val firebaseAnalytics = Firebase.analytics
    private val selectedVegeItem = viewModelScope.async { getSelectedVegeItemUseCase(args) }

    private val _uiState = MutableStateFlow(TakePictureScreenUiState.initialState())
    val uiState: StateFlow<TakePictureScreenUiState> = _uiState.asStateFlow()

    init {
        updateVegetableDetails()
        viewModelScope.launch {
            updateRegisterSelectDate()
        }
    }

    private fun updateVegetableDetails() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true
                )
            }
            getVegetableDetailsUseCase(args).collect { vegetableDetails ->
                _uiState.update {
                    it.copy(
                        isVisibleNavigateButton = vegetableDetails.isNotEmpty(),
                        lastSavedSize = vegetableDetails.lastOrNull()?.size,
                        isLoading = false,
                        vegeName = selectedVegeItem.await().name
                    )
                }
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
        _uiState.update {
            it.copy(
                isOpenDialog = false,
                inputText = "",
                isBeforeInputText = true,
                selectRegisterDate = null
            )
        }
    }

    fun registerVegeData() {
        val datetime = if (_uiState.value.selectRegisterDate != null) {
            dateFormatter.dateToString(_uiState.value.selectRegisterDate!!)
        } else {
            dateFormatter.dateToString(LocalDateTime.now())
        }
        // nullの時は，何も保存せずに保存していないと保存する
        val takePicImage = _uiState.value.takePicImage
        val imagePath = if (takePicImage != null) {
            vegetableDetailRepository.saveTookPicture(takePicImage)
        } else {
            "notSaved"
        }

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
                    imagePath = imagePath
                )
            vegetableDetailRepository.addVegeItemDetail(registerVegeItemDetail)
        }
        resetState()
        firebaseEventSend()
        _uiState.update {
            it.copy(selectRegisterDate = null)
        }
    }

    fun onTakePicture(takePicture: ImageProxy?) {
        takePicture ?: return
        val rotateTakePicture = fixRotateImage(takePic = takePicture)
        updateState(takePicImage = rotateTakePicture)

        analytics.logEvent(AnalyticsEvent.Analytics.takePicture())
    }

    fun changeInputText(inputText: String) {
        val isSuccessInputText = checkInputText(inputText = inputText)
        updateState(
            inputText = inputText,
            isSuccessInputText = isSuccessInputText,
            isBeforeInputText = false
        )
    }

    private fun checkInputText(inputText: String): Boolean = when (inputText.toDoubleOrNull()) {
        null -> false
        else -> true
    }

    private fun fixRotateImage(takePic: ImageProxy): Bitmap {
        val rotation = takePic.imageInfo.rotationDegrees
        val takePicBitMap = takePic.toBitmap()

        val matrix = Matrix()
        matrix.postRotate(rotation.toFloat())
        return Bitmap.createBitmap(
            takePicBitMap,
            0,
            0,
            takePic.width,
            takePic.height,
            matrix,
            true
        )
    }

    fun changeCameraOpenState() {
        _uiState.update {
            it.copy(
                isCameraOpen = !it.isCameraOpen
            )
        }
    }

    /**
     * 選択された登録する日付を変換する
     *
     * 選択されていない場合は何もしない
     */
    fun selectRegisterDate(registerDate: Long?) {
        registerDate ?: return
        val currentTime = ZonedDateTime.now()
        val registerDate = ZonedDateTime.of(
            Instant.ofEpochMilli(registerDate)
                .atZone(ZoneId.systemDefault())
                .toLocalDate(),
            currentTime.toLocalTime(),
            currentTime.zone
        )

        _uiState.update {
            it.copy(
                selectRegisterDate = registerDate
            )
        }
    }

    /**
     * 日付を登録するかを収集する
     */
    private suspend fun updateRegisterSelectDate() {
        isRegisterSelectDateUseCase().collect { isRegisterSelectDate ->
            _uiState.update {
                it.copy(
                    isRegisterSelectDate = isRegisterSelectDate
                )
            }
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
