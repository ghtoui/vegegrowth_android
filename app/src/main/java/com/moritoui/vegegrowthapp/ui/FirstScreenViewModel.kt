package com.moritoui.vegegrowthapp.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.moritoui.vegegrowthapp.model.FileManager
import com.moritoui.vegegrowthapp.model.VegeCategory
import com.moritoui.vegegrowthapp.model.VegeItem
import java.util.UUID
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class FirstScreenUiState(
    val isOpenDialog: Boolean = false,
    val inputText: String = "",
    val vegeItemList: MutableList<VegeItem> = mutableListOf()
)

class FirstScreenViewModel(
    applicationContext: Context
) : ViewModel() {
    private val fileManger: FileManager
    private var vegeItemList: MutableList<VegeItem>

    private val _uiState = MutableStateFlow(FirstScreenUiState())
    val uiState: StateFlow<FirstScreenUiState> = _uiState.asStateFlow()

    class FirstScreenFactory(
        private val applicationContext: Context
    ) : ViewModelProvider.Factory {
        @Suppress("unchecked_cast")
        override fun <T : ViewModel> create(modelClass: Class<T>) =
            FirstScreenViewModel(
                applicationContext
            ) as T
    }
    init {
        fileManger = FileManager(applicationContext = applicationContext)
        this.vegeItemList = fileManger.getVegeItemList()
        updateState(vegeItemList = vegeItemList)
    }

    private fun updateState(
        isOpenDialog: Boolean = _uiState.value.isOpenDialog,
        inputText: String = _uiState.value.inputText,
        vegeItemList: MutableList<VegeItem> = _uiState.value.vegeItemList
    ) {
        _uiState.update { currentState ->
            currentState.copy(
                isOpenDialog = isOpenDialog,
                inputText = inputText,
                vegeItemList = vegeItemList
            )
        }
    }

    fun closeDialog() {
        updateState(isOpenDialog = false)
    }

    fun openDialog() {
        updateState(isOpenDialog = true, inputText = "")
    }

    fun changeInputText(inputText: String) {
        updateState(inputText = inputText)
    }

    fun saveVegeItemListData() {
        vegeItemList.add(
            VegeItem(
                name = _uiState.value.inputText,
                category = VegeCategory.None,
                uuid = UUID.randomUUID().toString()
            )
        )
        fileManger.saveVegeItemListData(vegeItemList = vegeItemList)
        closeDialog()
    }
}
