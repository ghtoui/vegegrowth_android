package com.moritoui.vegegrowthapp.ui

import android.content.Context
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.moritoui.vegegrowthapp.model.FileManager
import com.moritoui.vegegrowthapp.model.SelectMenu
import com.moritoui.vegegrowthapp.model.SortStatus
import com.moritoui.vegegrowthapp.model.VegeCategory
import com.moritoui.vegegrowthapp.model.VegeItem
import com.moritoui.vegegrowthapp.model.VegeStatus
import com.moritoui.vegegrowthapp.model.sortStatusMap
import java.util.UUID
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class FirstScreenUiState(
    val isOpenAddDialog: Boolean = false,
    val inputText: String = "",
    val selectCategory: VegeCategory = VegeCategory.None,
    val isAddAble: Boolean = false,
    val selectMenu: SelectMenu = SelectMenu.None,
    val selectStatus: VegeStatus = VegeStatus.Default,
    val sortStatus: SortStatus = SortStatus.All
)

class FirstScreenViewModel(
    applicationContext: Context
) : ViewModel() {
    private val fileManger: FileManager
    private var deleteList: MutableList<VegeItem> = mutableListOf()

    private val _uiState = MutableStateFlow(FirstScreenUiState())
    val uiState: StateFlow<FirstScreenUiState> = _uiState.asStateFlow()

    private var _vegeItemList: MutableList<VegeItem>
    private var sortItemList: MutableList<VegeItem>
    val vegeItemList: MutableList<VegeItem>
        get() = sortItemList

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
        this._vegeItemList = fileManger.getVegeItemList().toMutableStateList()
        this.sortItemList = _vegeItemList
    }

    private fun updateState(
        isOpenAddDialog: Boolean = _uiState.value.isOpenAddDialog,
        inputText: String = _uiState.value.inputText,
        selectCategory: VegeCategory = _uiState.value.selectCategory,
        isAddAble: Boolean = _uiState.value.isAddAble,
        selectMenu: SelectMenu = _uiState.value.selectMenu,
        sortStatus: SortStatus = _uiState.value.sortStatus
    ) {
        _uiState.update { currentState ->
            currentState.copy(
                isOpenAddDialog = isOpenAddDialog,
                inputText = inputText,
                selectCategory = selectCategory,
                isAddAble = isAddAble,
                selectMenu = selectMenu,
                sortStatus = sortStatus
            )
        }
        sortVegeItemList()
    }

    fun closeDialog() {
        updateState(
            isOpenAddDialog = false
        )
    }

    fun openAddDialog() {
        updateState(
            isOpenAddDialog = true,
            inputText = "",
            selectCategory = VegeCategory.None
        )
    }

    fun selectStatus() {
        fileManger.saveVegeItemListData(vegeItemList = _vegeItemList)
    }

    fun changeInputText(inputText: String) {
        var isAddAble = false
        if (inputText != "") {
            isAddAble = true
        }
        updateState(
            inputText = inputText,
            isAddAble = isAddAble
        )
    }

    fun saveVegeItemListData() {
        _vegeItemList.add(
            VegeItem(
                name = _uiState.value.inputText,
                category = _uiState.value.selectCategory,
                uuid = UUID.randomUUID().toString(),
                status = VegeStatus.Default
            )
        )
        fileManger.saveVegeItemListData(vegeItemList = _vegeItemList)
        closeDialog()
    }

    fun changeDeleteMode() {
        if (_uiState.value.selectMenu == SelectMenu.Delete) {
            updateState(selectMenu = SelectMenu.None)
            deleteItemList()
        } else {
            updateState(selectMenu = SelectMenu.Delete)
        }
    }

    fun changeEditMode() {
        if (_uiState.value.selectMenu == SelectMenu.Edit) {
            updateState(selectMenu = SelectMenu.None)
        } else {
            updateState(selectMenu = SelectMenu.Edit)
        }
    }

    private fun deleteItemList() {
        deleteList.forEach { item ->
            _vegeItemList.remove(item)
        }
        deleteList = mutableListOf()
        fileManger.saveVegeItemListData(vegeItemList = vegeItemList)
    }

    fun deleteItem(item: VegeItem, isDelete: Boolean) {
        if (!isDelete) {
            deleteList.add(item)
        } else {
            deleteList.remove(item)
        }
    }

    fun cancelMenu() {
        deleteList = mutableListOf()
        updateState(selectMenu = SelectMenu.None)
    }

    fun selectCategory(selectCategory: VegeCategory) {
        updateState(selectCategory = selectCategory)
    }

    fun setSortItemList(sortStatus: SortStatus) {
        updateState(sortStatus = sortStatus)
    }

    private fun sortVegeItemList() {
        val sortStatus = _uiState.value.sortStatus
        sortItemList = when (sortStatus) {
            SortStatus.All -> _vegeItemList
            else -> {
                _vegeItemList.filter { item ->
                    item.status == sortStatusMap[sortStatus] ||
                            item.category == sortStatusMap[sortStatus]
                }.toMutableList()
            }
        }
    }
}
