package com.moritoui.vegegrowthapp.ui.folder

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moritoui.vegegrowthapp.data.room.model.VegetableFolderEntity
import com.moritoui.vegegrowthapp.model.FilterStatus
import com.moritoui.vegegrowthapp.model.SelectMenu
import com.moritoui.vegegrowthapp.model.VegeCategory
import com.moritoui.vegegrowthapp.model.VegeItem
import com.moritoui.vegegrowthapp.model.VegeItemDetail
import com.moritoui.vegegrowthapp.model.VegeStatus
import com.moritoui.vegegrowthapp.model.filterStatusMap
import com.moritoui.vegegrowthapp.ui.folder.model.FolderScreenUiState
import com.moritoui.vegegrowthapp.ui.home.model.AddDialogType
import com.moritoui.vegegrowthapp.ui.home.model.VegetablesState
import com.moritoui.vegegrowthapp.usecases.AddVegeItemUseCase
import com.moritoui.vegegrowthapp.usecases.ChangeVegeItemStatusUseCase
import com.moritoui.vegegrowthapp.usecases.DeleteVegeItemUseCase
import com.moritoui.vegegrowthapp.usecases.GetSelectedVegeFolderUseCase
import com.moritoui.vegegrowthapp.usecases.GetVegeItemDetailLastUseCase
import com.moritoui.vegegrowthapp.usecases.GetVegeItemFromFolderIdUseCase
import com.moritoui.vegegrowthapp.usecases.GetVegetableFolderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FolderScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getVegetableFromFolderIdUseCase: GetVegeItemFromFolderIdUseCase,
    private val getVegeItemDetailLastUseCase: GetVegeItemDetailLastUseCase,
    private val deleteVegeItemUseCase: DeleteVegeItemUseCase,
    private val addVegeItemUseCase: AddVegeItemUseCase,
    private val changeVegeItemStatusUseCase: ChangeVegeItemStatusUseCase,
    private val getSelectedVegeFolderUseCase: GetSelectedVegeFolderUseCase,
    private val getVegetableFolderUseCase: GetVegetableFolderUseCase,
) : ViewModel() {
    private val args = checkNotNull(savedStateHandle.get<Int>("folderId"))

    private val _uiState = MutableStateFlow(FolderScreenUiState.initial())
    val uiState: StateFlow<FolderScreenUiState> = _uiState.asStateFlow()

    private val _vegetables = MutableStateFlow<List<VegeItem>>(emptyList())
    private val _vegetableDetails = MutableStateFlow<List<VegeItemDetail?>>(emptyList())
    private val _vegetableFolders = MutableStateFlow<List<VegetableFolderEntity>>(emptyList())
    val vegetablesState: StateFlow<VegetablesState> = combine(
        _vegetables,
        _vegetableDetails,
        _vegetableFolders
    ) { vegetables, vegetableDetails, vegetableFolders ->
        VegetablesState(
            vegetables = vegetables,
            vegetableDetails = vegetableDetails,
            vegetableFolders = vegetableFolders
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        VegetablesState.initial()
    )

    init {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    selectedFolder = getSelectedVegeFolderUseCase(args)
                )
            }
        }
        monitorUiState()
    }

    fun closeDialog() {
        _uiState.update {
            it.copy(
                openAddDialogType = AddDialogType.NotOpenDialog
            )
        }
    }

    fun openAddDialog(addDialogType: AddDialogType) {
        _uiState.update {
            it.copy(
                openAddDialogType = addDialogType,
                inputText = "",
                selectCategory = VegeCategory.None
            )
        }
    }

    fun selectStatus(vegeItem: VegeItem) {
        changeVegeItem(vegeItem)
    }

    fun changeInputText(inputText: String) {
        val isAddAble = checkInputText(inputText = inputText)
        _uiState.update {
            it.copy(
                inputText = inputText,
                isAddAble = isAddAble
            )
        }
    }

    /**
     * 押されたダイアログの種類によって処理を変える
     */
    fun onAddDialogConfirm(addDialogType: AddDialogType) {
        when (addDialogType) {
            AddDialogType.AddVegeItem -> {
                val vegeItem =
                    VegeItem(
                        name = _uiState.value.inputText,
                        category = _uiState.value.selectCategory,
                        uuid = UUID.randomUUID().toString(),
                        status = VegeStatus.Default,
                        folderId = args
                    )
                viewModelScope.launch {
                    addVegeItemUseCase(vegeItem)
                    reloadVegetables()
                    closeDialog()
                }
            }
            else -> {
                return
            }
        }
    }

    fun changeDeleteMode() {
        _uiState.update {
            it.copy(
                selectMenu =
                if (_uiState.value.selectMenu == SelectMenu.Delete) {
                    SelectMenu.None
                } else {
                    SelectMenu.Delete
                }
            )
        }
    }

    fun changeEditMode() {
        _uiState.update {
            it.copy(
                selectMenu =
                if (_uiState.value.selectMenu == SelectMenu.Edit) {
                    SelectMenu.None
                } else {
                    SelectMenu.Edit
                }
            )
        }
    }

    fun changeFolderMoveMode() {
        _uiState.update {
            it.copy(
                selectMenu =
                if (_uiState.value.selectMenu == SelectMenu.Edit) {
                    SelectMenu.None
                } else {
                    SelectMenu.MoveFolder
                }
            )
        }
    }

    fun deleteItem() {
        viewModelScope.launch {
            val deleteItem = _uiState.value.selectedItem
            if (deleteItem != null) {
                deleteVegeItemUseCase(deleteItem)
            }
            _uiState.update {
                it.copy(
                    selectedItem = null
                )
            }
        }
    }

    fun onCancelMenuClick() {
        _uiState.update {
            it.copy(
                selectMenu = SelectMenu.None
            )
        }
    }

    fun selectCategory(selectCategory: VegeCategory) {
        _uiState.update {
            it.copy(
                selectCategory = selectCategory
            )
        }
    }

    // 現在選択されている
    fun setFilterItemList(filterStatus: FilterStatus) {
        _uiState.update {
            it.copy(
                filterStatus = filterStatus
            )
        }
    }

    // フォルダ選択モーダルを開く
    fun openFolderMoveBottomSheetState(vegeItem: VegeItem) {
        _uiState.update {
            it.copy(
                isOpenFolderMoveBottomSheet = true,
                selectedItem = vegeItem
            )
        }
    }

    // フォルダ選択モーダルを閉じる
    fun closeFolderMoveBottomSheetState() {
        _uiState.update {
            it.copy(
                isOpenFolderMoveBottomSheet = false
            )
        }
    }

    /**
     * フォルダ移動をする
     */
    fun vegeItemMoveFolder(vegeItem: VegeItem) {
        changeVegeItem(vegeItem)
    }

    /**
     * 削除ダイアログを閉じる
     */
    fun closeDeleteDialog() {
        _uiState.update {
            it.copy(
                isOpenDeleteDialog = !it.isOpenDeleteDialog
            )
        }
    }

    /**
     * 削除ダイアログを開いて，削除するものをセットする
     */
    fun openDeleteVegeItemDialog(vegeItem: VegeItem) {
        _uiState.update {
            it.copy(
                isOpenDeleteDialog = true,
                selectedItem = vegeItem
            )
        }
    }

    /**
     * 登録されている野菜のリストを更新する
     */
    fun reloadVegetables() {
        viewModelScope.launch {
            val filterStatus = _uiState.value.filterStatus
            val filteredVegetables = getVegetableFromFolderIdUseCase(args).filter { item ->
                if (filterStatus == FilterStatus.All) {
                    true
                } else {
                    item.status == filterStatusMap[filterStatus] ||
                        item.category == filterStatusMap[filterStatus]
                }
            }

            _vegetables.update {
                filteredVegetables
            }

            _vegetableDetails.update {
                filteredVegetables.map { reloadVegetableDetailLast(it) }
            }

            _vegetableFolders.update {
                getVegetableFolderUseCase()
            }
        }
    }

    /**
     * 指定された野菜の最新登録情報を取得する
     */
    private suspend fun reloadVegetableDetailLast(vegeItem: VegeItem): VegeItemDetail? = getVegeItemDetailLastUseCase(vegeItem.id)

    /**
     * uiStateの変更を監視する
     */
    private fun monitorUiState() {
        _uiState.onEach {
            _uiState.update {
                it.copy(isLoading = true)
            }
            reloadVegetables()
            _uiState.update {
                it.copy(isLoading = false)
            }
        }.launchIn(viewModelScope)
    }

    private fun checkInputText(inputText: String): Boolean = when (inputText) {
        "" -> false
        else -> true
    }

    /**
     * 登録されている野菜の情報を更新する
     */
    private fun changeVegeItem(vegeItem: VegeItem) {
        viewModelScope.launch {
            changeVegeItemStatusUseCase(vegeItem)
            _vegetables.value.map { old ->
                if (old.id == vegeItem.id) {
                    vegeItem
                } else {
                    old
                }
            }
            reloadVegetables()
        }
    }
}
