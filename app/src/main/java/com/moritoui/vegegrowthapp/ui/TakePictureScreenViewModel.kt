package com.moritoui.vegegrowthapp.ui

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.moritoui.vegegrowthapp.model.VegeItem
import com.moritoui.vegegrowthapp.model.VegeItemList
import com.moritoui.vegegrowthapp.model.VegetableRepository
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException
import java.io.OutputStream
import java.util.UUID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

data class TakePictureScreenUiState(
    val vegeName: String = "",
    val isOpenDialog: Boolean = false,
    val inputText: String = "",
    val isSuccessInputText: Boolean = false,
    val isBeforeInputText: Boolean = true,
    val takePicImage: Bitmap? = null
)

class TakePictureScreenViewModel constructor(
    index: Int,
    private val applicationContext: Context
) : ViewModel() {
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
        this.vegeItem = getVegeItem(index)
        _uiState.update { currentState ->
            currentState.copy(vegeName = this.vegeItem.name)
        }
        viewModelScope.launch(Dispatchers.IO) {
            vegeList = parseFromJson(readJsonData())
        }
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
        // そもそもボタンが押せないようにしているから、inputTextとtakePicImageはnullにならないはず
        vegeList.add(
            VegetableRepository(
                itemUuid = vegeItem.uuid.toString(),
                uuid = UUID.randomUUID().toString(),
                name = vegeItem.name,
                size = _uiState.value.inputText.toDouble(),
                memo = "",
            )
        )
        saveData()
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

    private fun saveData() {
        val imageFileName = "${vegeItem.uuid}.jpg"
        val imageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val imageFilePath = File(imageDirectory, imageFileName)
        val outputStream: OutputStream = FileOutputStream(imageFilePath)
        _uiState.value.takePicImage?.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)

        val jsonFileName = "${vegeItem.uuid}.json"
        val jsonFilePath = File(applicationContext.filesDir, jsonFileName)
        FileWriter(jsonFilePath).use { stream ->
            stream.write(parseToJson())
        }
    }

    private suspend fun readJsonData(): String? {
        var json: String? = null
        val jsonFileName = "${vegeItem.uuid}.json"
        val jsonFilePath = File(applicationContext.filesDir, jsonFileName)
        try {
            BufferedReader(FileReader(jsonFilePath)).use { br ->
                json = br.readLine()
            }
        } catch (e: IOException) {
            Log.d("Error", "File Read Error$jsonFilePath")
        }
        return json
    }

    private fun parseToJson(): String {
        return Json.encodeToString(vegeList)
    }

    private fun parseFromJson(json: String?): MutableList<VegetableRepository> {
        return when (json) {
            null -> mutableListOf<VegetableRepository>()
            else -> Json.decodeFromString(json)
        }
    }

    fun getVegeItem(index: Int): VegeItem {
        return VegeItemList.getVegeList()[index]
    }
}
