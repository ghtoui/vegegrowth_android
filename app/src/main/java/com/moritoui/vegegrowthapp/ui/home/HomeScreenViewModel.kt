package com.moritoui.vegegrowthapp.ui.home

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
import com.moritoui.vegegrowthapp.repository.datamigration.DataMigrationRepository
import com.moritoui.vegegrowthapp.ui.home.model.AddDialogType
import com.moritoui.vegegrowthapp.ui.home.model.HomeScreenUiState
import com.moritoui.vegegrowthapp.ui.home.model.VegetablesState
import com.moritoui.vegegrowthapp.usecases.AddVegeItemUseCase
import com.moritoui.vegegrowthapp.usecases.ChangeRegisterSelectDateUseCase
import com.moritoui.vegegrowthapp.usecases.ChangeVegeItemStatusUseCase
import com.moritoui.vegegrowthapp.usecases.DeleteVegeFolderUseCase
import com.moritoui.vegegrowthapp.usecases.DeleteVegeItemUseCase
import com.moritoui.vegegrowthapp.usecases.GetVegeItemDetailLastUseCase
import com.moritoui.vegegrowthapp.usecases.GetVegeItemFromFolderIdUseCase
import com.moritoui.vegegrowthapp.usecases.GetVegetableFolderUseCase
import com.moritoui.vegegrowthapp.usecases.InsertVegetableFolderUseCase
import com.moritoui.vegegrowthapp.usecases.IsRegisterSelectDateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
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
class HomeScreenViewModel @Inject constructor(
    private val addVegeItemUseCase: AddVegeItemUseCase,
    private val deleteVegeItemUseCase: DeleteVegeItemUseCase,
    private val deleteVegeFolderUseCase: DeleteVegeFolderUseCase,
    private val getVegeItemFromFolderIdUseCase: GetVegeItemFromFolderIdUseCase,
    private val changeVegeItemStatusUseCase: ChangeVegeItemStatusUseCase,
    private val getVegeItemDetailLastUseCase: GetVegeItemDetailLastUseCase,
    private val dataMigrationRepository: DataMigrationRepository,
    private val getVegetableFolderUseCase: GetVegetableFolderUseCase,
    private val insertVegetableFolderUseCase: InsertVegetableFolderUseCase,
    private val isRegisterSelectDateUseCase: IsRegisterSelectDateUseCase,
    private val changeRegisterSelectDateUseCase: ChangeRegisterSelectDateUseCase,
    private val analytics: AnalyticsHelper,
) : ViewModel() {
    // ホーム画面では未分類のフォルダーIDnullのみを表示する
    private val folderId = null

    private val _uiState = MutableStateFlow(HomeScreenUiState.initialState())
    val uiState: StateFlow<HomeScreenUiState> = _uiState.asStateFlow()

    private val _isDataMigrating = dataMigrationRepository.isDataMigrating
    val isDataMigrating: StateFlow<Boolean> = _isDataMigrating.asStateFlow()

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

    /**
     * インサートエラーかどうか
     */
    private val _insertVegetableFolderEvent: MutableSharedFlow<Boolean> = MutableSharedFlow()
    val insertVegetableFolderEvent: Flow<Boolean> = _insertVegetableFolderEvent.asSharedFlow()

    init {
        viewModelScope.launch {
            dataMigrationRepository.dataMigration()
            updateRegisterSelectDate()
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
                        folderId = null
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
            AddDialogType.AddFolder -> {
                val vegeFolder = VegetableFolderEntity(
                    id = 0,
                    folderNumber = _vegetableFolders.value.size,
                    folderName = _uiState.value.inputText,
                    vegetableCategory = _uiState.value.selectCategory
                )
                viewModelScope.launch {
                    insertVegetableFolderUseCase(vegeFolder)
                        .onSuccess {
                            reloadVegetables()
                            closeDialog()
                        }.onFailure {
                            _insertVegetableFolderEvent.emit(true)
                            delay(2000)
                            _insertVegetableFolderEvent.emit(false)
                        }
                }
                analytics.logEvent(AnalyticsEvent.Analytics.createFolder(vegeFolder.folderName))
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
            val deleteFolder = _uiState.value.selectedFolder
            if (deleteItem != null) {
                deleteVegeItemUseCase(deleteItem)
                analytics.logEvent(AnalyticsEvent.Analytics.deleteItem(deleteItem.name))
            }
            if (deleteFolder != null) {
                deleteVegeFolderUseCase(deleteFolder)
                analytics.logEvent(AnalyticsEvent.Analytics.deleteFolder(deleteFolder.folderName))
            }
            _uiState.update {
                it.copy(
                    selectedItem = null,
                    selectedFolder = null
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

        if (_uiState.value.selectedItem != null) {
            analytics.logEvent(AnalyticsEvent.Analytics.cancelDialog(DialogType.DELETE_ITEM.name))
        } else if (_uiState.value.selectedFolder != null) {
            analytics.logEvent(AnalyticsEvent.Analytics.cancelDialog(DialogType.DELETE_FOLDER.name))
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

        analytics.logEvent(AnalyticsEvent.Analytics.openDialog(DialogType.DELETE_ITEM.name))
    }

    /**
     * 削除ダイアログを開いて，削除するものをセットする
     */
    fun openDeleteFolderDialog(folder: VegetableFolderEntity) {
        _uiState.update {
            it.copy(
                isOpenDeleteDialog = true,
                selectedFolder = folder
            )
        }

        analytics.logEvent(AnalyticsEvent.Analytics.openDialog(DialogType.DELETE_FOLDER.name))
    }

    private fun checkInputText(inputText: String): Boolean = when (inputText) {
        "" -> false
        else -> true
    }

    /**
     * 登録されている野菜のリストを更新する
     */
    fun reloadVegetables() {
        viewModelScope.launch {
            val filterStatus = _uiState.value.filterStatus
            val filteredVegetables = getVegeItemFromFolderIdUseCase(folderId).filter { item ->
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
     * 日付選択切り替えのトグルボタンが押された
     */
    fun onRegisterDateSwitch(isRegisterSelectDate: Boolean) {
        viewModelScope.launch {
            changeRegisterSelectDateUseCase(isRegisterSelectDate)
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
