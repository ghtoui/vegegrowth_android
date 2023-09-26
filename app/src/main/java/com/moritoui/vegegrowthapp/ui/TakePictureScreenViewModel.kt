package com.moritoui.vegegrowthapp.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.moritoui.vegegrowthapp.model.DateFormatter
import com.moritoui.vegegrowthapp.model.VegeItem
import com.moritoui.vegegrowthapp.model.VegetableRepository
import com.moritoui.vegegrowthapp.model.VegetableRepositoryFileManager
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
    val takePicImage: Bitmap? = null,
    val isVisibleNavigateButton: Boolean = false
)

class TakePictureScreenViewModel constructor(
    private val index: Int,
    applicationContext: Context
) : ViewModel() {
    private val dateFormatter = DateFormatter()
    private val fileManager: VegetableRepositoryFileManager

    private var vegeRepositoryList: MutableList<VegetableRepository>
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
        this.fileManager = VegetableRepositoryFileManager(
            index = index,
            applicationContext = applicationContext
        )
        this.vegeItem = fileManager.getVegeItem()
        _uiState.update { currentState ->
            currentState.copy(vegeName = this.vegeItem.name)
        }
        this.vegeRepositoryList = fileManager.getVegeRepositoryList()
        updateState(isVisibleNavigateButton = vegeRepositoryList.isNotEmpty())
    }

    private fun updateState(
        isOpenDialog: Boolean = _uiState.value.isOpenDialog,
        inputText: String = _uiState.value.inputText,
        isSuccessInputText: Boolean = _uiState.value.isSuccessInputText,
        isBeforeInputText: Boolean = _uiState.value.isBeforeInputText,
        takePicImage: Bitmap? = _uiState.value.takePicImage,
        isVisibleNavigateButton: Boolean = _uiState.value.isVisibleNavigateButton
    ) {
        _uiState.update { currentState ->
            currentState.copy(
                isOpenDialog = isOpenDialog,
                inputText = inputText,
                isSuccessInputText = isSuccessInputText,
                isBeforeInputText = isBeforeInputText,
                takePicImage = takePicImage,
                isVisibleNavigateButton = isVisibleNavigateButton
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
            VegetableRepository(
                itemUuid = vegeItem.uuid.toString(),
                uuid = UUID.randomUUID().toString(),
                name = vegeItem.name,
                size = _uiState.value.inputText.toDouble(),
                memo = "",
                date = datetime
            )
        )
        fileManager.saveVegeRepositoryAndImage(vegeRepositoryList = vegeRepositoryList, takePicImage = _uiState.value.takePicImage)
        resetState()
        updateState(isVisibleNavigateButton = vegeRepositoryList.isNotEmpty())
    }

    fun setImage(takePicImage: Bitmap?) {
        val fileName = "tempImage"
        fileManager.saveImage(takePicImage = takePicImage, fileName = fileName)
        val filePath = fileManager.getImagePath(fileName = fileName)
        val rotateTakePicture = rotateBitmapIfNeeded(filePath = filePath, bitmap = takePicImage!!)
        updateState(takePicImage = rotateTakePicture)
    }

    private fun rotateBitmapIfNeeded(filePath: String, bitmap: Bitmap): Bitmap {
        val exif = ExifInterface(filePath)
        val rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

        // TODO: ここ毎回0になって横向きになってしまうから、無理やり90度回転させている
        val degrees = when (rotation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 90
        }
        return if (degrees != 0) {
            val matrix = Matrix()
            matrix.postRotate(degrees.toFloat())
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        } else {
            bitmap
        }
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
