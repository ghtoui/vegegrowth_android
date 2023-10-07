package com.moritoui.vegegrowthapp.ui

import android.content.Context
import androidx.compose.runtime.toMutableStateList
import com.moritoui.vegegrowthapp.di.FirstScreenUiState
import com.moritoui.vegegrowthapp.di.FirstViewModel
import com.moritoui.vegegrowthapp.model.FileManager
import com.moritoui.vegegrowthapp.model.SelectMenu
import com.moritoui.vegegrowthapp.model.SortStatus
import com.moritoui.vegegrowthapp.model.VegeCategory
import com.moritoui.vegegrowthapp.model.VegeItem
import com.moritoui.vegegrowthapp.model.VegeStatus
import java.util.UUID
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FirstScreenViewModel(
    applicationContext: Context
) : FirstViewModel {
    private val fileManger: FileManager
    private var deleteList: MutableList<VegeItem> = mutableListOf()

    private val _uiState = MutableStateFlow(FirstScreenUiState())
    override val uiState: StateFlow<FirstScreenUiState> = _uiState.asStateFlow()

    private var _vegeItemList: MutableList<VegeItem>
    private var sortItemList: MutableList<VegeItem>
    override val vegeItemList: MutableList<VegeItem>
        get() = sortItemList

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
        sortItemList = sortList(
            sortStatus = _uiState.value.sortStatus,
            itemList = _vegeItemList
        )
    }

    override fun closeDialog() {
        updateState(
            isOpenAddDialog = false
        )
    }

    override fun openAddDialog() {
        updateState(
            isOpenAddDialog = true,
            inputText = "",
            selectCategory = VegeCategory.None
        )
    }

    override fun selectStatus() {
        fileManger.saveVegeItemListData(vegeItemList = _vegeItemList)
    }

    override fun changeInputText(inputText: String) {
        val isAddAble = checkInputText(inputText = inputText)
        updateState(
            inputText = inputText,
            isAddAble = isAddAble
        )
    }

    override fun saveVegeItemListData() {
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

    override fun changeDeleteMode() {
        if (_uiState.value.selectMenu == SelectMenu.Delete) {
            updateState(selectMenu = SelectMenu.None)
            deleteItemList()
        } else {
            updateState(selectMenu = SelectMenu.Delete)
        }
    }

    override fun changeEditMode() {
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

    override fun deleteItem(item: VegeItem, isDelete: Boolean) {
        if (!isDelete) {
            deleteList.add(item)
        } else {
            deleteList.remove(item)
        }
    }

    override fun cancelMenu() {
        deleteList = mutableListOf()
        updateState(selectMenu = SelectMenu.None)
    }

    override fun selectCategory(selectCategory: VegeCategory) {
        updateState(selectCategory = selectCategory)
    }

    override fun setSortItemList(sortStatus: SortStatus) {
        updateState(sortStatus = sortStatus)
    }
}
