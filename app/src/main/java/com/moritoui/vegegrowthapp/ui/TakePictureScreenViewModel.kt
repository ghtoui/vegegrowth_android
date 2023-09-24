package com.moritoui.vegegrowthapp.ui

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.moritoui.vegegrowthapp.model.DateFormatter
import com.moritoui.vegegrowthapp.model.FileManager
import com.moritoui.vegegrowthapp.model.VegeItem
import com.moritoui.vegegrowthapp.model.VegetableRepository
import java.time.LocalDateTime
import java.util.UUID
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class TakePictureScreenUiState(
    val vegeName: String = "",
    val isOpenDialog: Boolean = false,
    val inputText: String = "",
    val isSuccessInputText: Boolean = false,
    val isBeforeInputText: Boolean = true,
    val takePicImage: Bitmap? = null
)

class TakePictureScreenViewModel constructor(
    private val index: Int,
    applicationContext: Context
) : ViewModel() {
    private val dateFormatter = DateFormatter()
    private val fileManager: FileManager

    private var vegeList: MutableList<VegetableRepository> = mutableListOf()
    private var vegeItem: VegeItem

    private val _uiState = MutableStateFlow(TakePictureScreenUiState())
    val uiState: StateFlow<TakePictureScreenUiState> = _uiState.asStateFlow()

    class TakePictureFactory(
        private val index: Int,
        private val applicationContext: Context
    ) : ViewModelProvider.Factory {
        @Suppress("unchecked_cast")
        override fun <T : ViewModel> create(modelClass: Class<T>) =
            TakePictureScreenViewModel(
                index,
                applicationContext
            ) as T
    }

    init {
        this.fileManager = FileManager(
            index = index,
            applicationContext = applicationContext
        )
        this.vegeItem = fileManager.getVegeItem()
        _uiState.update { currentState ->
            currentState.copy(vegeName = this.vegeItem.name)
        }
        vegeList = fileManager.parseFromJson(fileManager.readJsonData())
    }

    private fun updateState(
        isOpenDialog: Boolean = _uiState.value.isOpenDialog,
        inputText: String = _uiState.value.inputText,
        isSuccessInputText: Boolean = _uiState.value.isSuccessInputText,
        isBeforeInputText: Boolean = _uiState.value.isBeforeInputText,
        takePicImage: Bitmap? = _uiState.value.takePicImage
    ) {
        _uiState.update { currentState ->
            currentState.copy(
                isOpenDialog = isOpenDialog,
                inputText = inputText,
                isSuccessInputText = isSuccessInputText,
                isBeforeInputText = isBeforeInputText,
                takePicImage = takePicImage
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
        vegeList.add(
            VegetableRepository(
                itemUuid = vegeItem.uuid.toString(),
                uuid = UUID.randomUUID().toString(),
                name = vegeItem.name,
                size = _uiState.value.inputText.toDouble(),
                memo = "",
                date = datetime
            )
        )
        fileManager.saveData(vegeList = vegeList, takePicImage = _uiState.value.takePicImage)
        resetState()
    }

    fun setImage(takePicImage: Bitmap?) {
        updateState(takePicImage = takePicImage)
    }

    fun checkInputText(inputText: String) {
        val isSuccessInputText = when (inputText.toDoubleOrNull()) {
            null -> false
            else -> true
        }
        updateState(
            inputText = inputText,
            isSuccessInputText = isSuccessInputText,
            isBeforeInputText = false
        )
    }

    fun getIndex(): Int {
        return index
    }
}