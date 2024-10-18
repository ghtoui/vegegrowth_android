package com.moritoui.vegegrowthapp.ui.folder

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moritoui.vegegrowthapp.core.analytics.AnalyticsEvent
import com.moritoui.vegegrowthapp.core.analytics.AnalyticsHelper
import com.moritoui.vegegrowthapp.data.room.model.VegetableFolderEntity
import com.moritoui.vegegrowthapp.model.DialogType
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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
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
    private val analytics: AnalyticsHelper,
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
        reloadVegetableDetailLast()
    }

    fun closeDialog() {
        val dialogType = _uiState.value.openAddDialogType
        _uiState.update {
            it.copy(
                openAddDialogType = AddDialogType.NotOpenDialog
            )
        }

        dialogEvent(
            dialogEvent = { AnalyticsEvent.Analytics.cancelDialog(it) },
            dialogType = dialogType
        )
    }

    fun openAddDialog(addDialogType: AddDialogType) {
        _uiState.update {
            it.copy(
                openAddDialogType = addDialogType,
                inputText = "",
                selectCategory = VegeCategory.None
            )
        }

        dialogEvent(
            dialogEvent = { analytics.logEvent(AnalyticsEvent.Analytics.openDialog(it)) },
            dialogType = addDialogType
        )
    }

    fun selectStatus(vegeItem: VegeItem) {
        changeVegeItem(vegeItem)

        analytics.logEvent(
            AnalyticsEvent.Analytics.changeStatus(
                status = vegeItem.status,
                itemName = vegeItem.name
            )
        )
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

                analytics.logEvent(
                    AnalyticsEvent.Analytics.createItem(
                        itemName = vegeItem.name,
                        category = vegeItem.category
                    )
                )
            }
            else -> {
                return
            }
        }

        dialogEvent(
            dialogEvent = { analytics.logEvent(AnalyticsEvent.Analytics.confirmDialog(it)) },
            dialogType = addDialogType
        )
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
                analytics.logEvent(AnalyticsEvent.Analytics.deleteItem(deleteItem.name))
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
    fun vegeItemMoveFolder(vegeItem: VegeItem, folder: VegetableFolderEntity?) {
        changeVegeItem(vegeItem)

        analytics.logEvent(
            AnalyticsEvent.Analytics.moveFolder(
                folderName = folder?.folderName ?: "フォルダ設定なし",
                itemName = vegeItem.name
            )
        )
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

        analytics.logEvent(AnalyticsEvent.Analytics.cancelDialog(DialogType.DELETE_ITEM.name))
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

        analytics.logEvent(AnalyticsEvent.Analytics.openDialog(DialogType.DELETE_ITEM.name))
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

            _vegetableFolders.update {
                getVegetableFolderUseCase()
            }
        }
    }

    /**
     * 指定された野菜の最新登録情報を取得する
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    private fun reloadVegetableDetailLast() {
        viewModelScope.launch {
            _vegetables.flatMapLatest { vegetables ->
                combine(vegetables.map { vegetable ->
                    getVegeItemDetailLastUseCase(vegetable.id)
                }) {
                    it.toList()
                }
            }.collect {
                _vegetableDetails.value = it
            }
        }
    }

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

    /**
     * ダイアログの種類をしてイベントを送る
     */
    private fun dialogEvent(dialogEvent: (String) -> Unit, dialogType: AddDialogType) {
        when (dialogType) {
            AddDialogType.AddFolder -> {
                dialogEvent(DialogType.FOLDER.name)
            }
            AddDialogType.AddVegeItem -> {
                dialogEvent(DialogType.VEGE_ITEM.name)
            }
            else -> Unit // NOOP
        }
    }
}
