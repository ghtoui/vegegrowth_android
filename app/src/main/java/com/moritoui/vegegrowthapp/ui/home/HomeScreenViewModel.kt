package com.moritoui.vegegrowthapp.ui.home

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
import com.moritoui.vegegrowthapp.repository.datamigration.DataMigrationRepository
import com.moritoui.vegegrowthapp.ui.home.model.AddDialogType
import com.moritoui.vegegrowthapp.ui.home.model.HomeScreenUiState
import com.moritoui.vegegrowthapp.ui.home.model.HomeVegetablesState
import com.moritoui.vegegrowthapp.usecases.AddVegeItemUseCase
import com.moritoui.vegegrowthapp.usecases.ChangeVegeItemStatusUseCase
import com.moritoui.vegegrowthapp.usecases.DeleteVegeFolderUseCase
import com.moritoui.vegegrowthapp.usecases.DeleteVegeItemUseCase
import com.moritoui.vegegrowthapp.usecases.GetVegeItemDetailLastUseCase
import com.moritoui.vegegrowthapp.usecases.GetVegeItemListUseCase
import com.moritoui.vegegrowthapp.usecases.GetVegetableFolderUseCase
import com.moritoui.vegegrowthapp.usecases.InsertVegetableFolderUseCase
import com.moritoui.vegegrowthapp.usecases.UpdateVegetableFolderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
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
    private val getVegeItemListUseCase: GetVegeItemListUseCase,
    private val changeVegeItemStatusUseCase: ChangeVegeItemStatusUseCase,
    private val getVegeItemDetailLastUseCase: GetVegeItemDetailLastUseCase,
    private val dataMigrationRepository: DataMigrationRepository,
    private val getVegetableFolderUseCase: GetVegetableFolderUseCase,
    private val insertVegetableFolderUseCase: InsertVegetableFolderUseCase,
    private val updateVegetableFolderUseCase: UpdateVegetableFolderUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeScreenUiState.initialState())
    val uiState: StateFlow<HomeScreenUiState> = _uiState.asStateFlow()

    private val _isDataMigrating = dataMigrationRepository.isDataMigrating
    val isDataMigrating: StateFlow<Boolean> = _isDataMigrating.asStateFlow()

    private val _vegetables = MutableStateFlow<List<VegeItem>>(emptyList())
    private val _vegetableDetails = MutableStateFlow<List<VegeItemDetail?>>(emptyList())
    private val _vegetableFolders = MutableStateFlow<List<VegetableFolderEntity>>(emptyList())
    val vegetablesState: StateFlow<HomeVegetablesState> = combine(
        _vegetables,
        _vegetableDetails,
        _vegetableFolders
    ) { vegetables, vegetableDetails, vegetableFolders ->
        HomeVegetablesState(
            vegetables = vegetables,
            vegetableDetails = vegetableDetails,
            vegetableFolders = vegetableFolders
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        HomeVegetablesState.initial()
    )

    /**
     * インサートエラーかどうか
     */
    private val _insertVegetableFolderEvent: MutableSharedFlow<Boolean> = MutableStateFlow(false)
    val insertVegetableFolderEvent: SharedFlow<Boolean> = _insertVegetableFolderEvent.asSharedFlow()

    init {
        viewModelScope.launch {
            dataMigrationRepository.dataMigration()
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

    fun deleteItem() {
        viewModelScope.launch {
            val deleteItem = _uiState.value.targetDeleteItem
            val deleteFolder = _uiState.value.targetDeleteFolder
            if (deleteItem != null) {
                deleteVegeItemUseCase(deleteItem)
            }
            if (deleteFolder != null) {
                deleteVegeFolderUseCase(deleteFolder)
            }
            _uiState.update {
                it.copy(
                    targetDeleteItem = null,
                    targetDeleteFolder = null
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
                targetDeleteItem = vegeItem
            )
        }
    }

    /**
     * 削除ダイアログを開いて，削除するものをセットする
     */
    fun openDeleteFolderDialog(folder: VegetableFolderEntity) {
        _uiState.update {
            it.copy(
                isOpenDeleteDialog = true,
                targetDeleteFolder = folder
            )
        }
    }

    private fun checkInputText(inputText: String): Boolean = when (inputText) {
        "" -> false
        else -> true
    }

    /**
     * 登録されている野菜のリストを更新する
     */
    private fun reloadVegetables() {
        viewModelScope.launch {
            val filterStatus = _uiState.value.filterStatus
            val filteredVegetables = getVegeItemListUseCase().filter { item ->
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
}
