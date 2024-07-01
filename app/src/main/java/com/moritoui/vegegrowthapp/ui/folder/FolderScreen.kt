package com.moritoui.vegegrowthapp.ui.folder

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.moritoui.vegegrowthapp.R
import com.moritoui.vegegrowthapp.dummies.HomeScreenDummy
import com.moritoui.vegegrowthapp.dummies.ManageScreenDummy
import com.moritoui.vegegrowthapp.model.FilterStatus
import com.moritoui.vegegrowthapp.model.VegeCategory
import com.moritoui.vegegrowthapp.model.VegeItem
import com.moritoui.vegegrowthapp.navigation.HomeAddItem
import com.moritoui.vegegrowthapp.navigation.NavigationAppTopBar
import com.moritoui.vegegrowthapp.previews.DarkLightPreview
import com.moritoui.vegegrowthapp.ui.common.VegeGrowthLoading
import com.moritoui.vegegrowthapp.ui.common.bottomsheet.FolderMoveBottomSheet
import com.moritoui.vegegrowthapp.ui.folder.model.FolderScreenUiState
import com.moritoui.vegegrowthapp.ui.home.model.AddDialogType
import com.moritoui.vegegrowthapp.ui.home.model.VegetablesState
import com.moritoui.vegegrowthapp.ui.home.view.AddTextCategoryDialog
import com.moritoui.vegegrowthapp.ui.home.view.ConfirmDeleteItemDialog
import com.moritoui.vegegrowthapp.ui.home.view.ItemListTopBar
import com.moritoui.vegegrowthapp.ui.home.view.VegeItemListCard
import com.moritoui.vegegrowthapp.ui.takepicture.navigateToTakePicture
import com.moritoui.vegegrowthapp.ui.theme.VegegrowthAppTheme
import kotlinx.coroutines.launch

@Composable
fun FolderScreen(
    viewModel: FolderScreenViewModel = hiltViewModel(),
    navController: NavController,
) {
    val uiState by viewModel.uiState.collectAsState()
    val vegetablesState by viewModel.vegetablesState.collectAsState()
    FolderScreen(
        uiState = uiState,
        vegetablesState = vegetablesState,
        onBackNavigationButtonClick = navController::popBackStack,
        openAddDialogType = viewModel::openAddDialog,
        onCancelMenuClick = viewModel::onCancelMenuClick,
        onDeleteVegeItem = viewModel::changeDeleteMode,
        onEditIconClick = viewModel::changeEditMode,
        onFolderMoveIconClick = viewModel::changeFolderMoveMode,
        onFilterItemClick = viewModel::setFilterItemList,
        confirmItemDelete = viewModel::deleteItem,
        onSelectVegeStatus = viewModel::selectStatus,
        onVegeItemClick = navController::navigateToTakePicture,
        changeInputText = viewModel::changeInputText,
        closeDeleteDialog = viewModel::closeDeleteDialog,
        openDeleteDialog = viewModel::openDeleteVegeItemDialog,
        onAddDialogConfirmClick = viewModel::onAddDialogConfirm,
        onDismiss = viewModel::closeDialog,
        onSelectVegeCategory = viewModel::selectCategory,
        onSelectMoveFolder = viewModel::openFolderMoveBottomSheetState,
        closeFolderBottomSheet = viewModel::closeFolderMoveBottomSheetState,
        onFolderItemClick = viewModel::vegeItemMoveFolder,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FolderScreen(
    uiState: FolderScreenUiState,
    vegetablesState: VegetablesState,
    onBackNavigationButtonClick: () -> Unit,
    openAddDialogType: (AddDialogType) -> Unit,
    onCancelMenuClick: () -> Unit,
    onDeleteVegeItem: () -> Unit,
    onEditIconClick: () -> Unit,
    onFolderMoveIconClick: () -> Unit,
    onSelectMoveFolder: (VegeItem) -> Unit,
    onFilterItemClick: (FilterStatus) -> Unit,
    confirmItemDelete: () -> Unit,
    onSelectVegeStatus: (VegeItem) -> Unit,
    onVegeItemClick: (Int) -> Unit,
    changeInputText: (String) -> Unit,
    closeDeleteDialog: () -> Unit,
    openDeleteDialog: (VegeItem) -> Unit,
    onAddDialogConfirmClick: (AddDialogType) -> Unit,
    onDismiss: () -> Unit,
    onSelectVegeCategory: (VegeCategory) -> Unit,
    closeFolderBottomSheet: () -> Unit,
    onFolderItemClick: (VegeItem) -> Unit,
) {
    var selectMenuExpanded by rememberSaveable { mutableStateOf(false) }
    var filterMenuExpanded by rememberSaveable { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            NavigationAppTopBar(
                title = uiState.selectedFolder?.folderName ?: "",
                actions = {
                    HomeAddItem(
                        onAddClick = { openAddDialogType(AddDialogType.AddVegeItem) }
                    )
                },
                isVisibleBackButton = true,
                onBackNavigationButtonClick = onBackNavigationButtonClick,
            )
        }
    ) { it ->
        Box(
            modifier = Modifier
                .padding(top = it.calculateTopPadding())
                .padding(horizontal = 12.dp)
        ) {
            Scaffold(
                topBar = {
                    ItemListTopBar(
                        onCancelClick = onCancelMenuClick,
                        onDeleteIconClick = onDeleteVegeItem,
                        onEditIconClick = onEditIconClick,
                        onFolderMoveIconClick = onFolderMoveIconClick,
                        onFilterItemClick = onFilterItemClick,
                        selectMenuExpanded = selectMenuExpanded,
                        filterMenuExpanded = filterMenuExpanded,
                        onSelectDropDownMenuClose = { selectMenuExpanded = false },
                        onFilterDropDownMenuClose = { filterMenuExpanded = false },
                        onSelectMenuClick = { selectMenuExpanded = true },
                        onFilterMenuClick = { filterMenuExpanded = true },
                        selectMenu = uiState.selectMenu
                    )
                }
            ) { it ->
                if (uiState.isLoading) {
                    VegeGrowthLoading(
                        modifier = Modifier
                            .padding(top = it.calculateTopPadding())
                            .fillMaxSize()
                    )
                }
                LazyColumn(
                    modifier = Modifier.padding(top = it.calculateTopPadding(), bottom = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    this.items(
                        vegetablesState.vegetables.zip(vegetablesState.vegetableDetails),
                        key = { vegetable -> "${vegetable.first.id}, ${vegetable.first.name}" }
                    ) { vegetable ->
                        VegeItemListCard(
                            vegetable = vegetable.first,
                            vegetableDetail = vegetable.second,
                            onVegeItemClick = { onVegeItemClick(it) },
                            selectMenu = uiState.selectMenu,
                            onItemDeleteClick = { item ->
                                openDeleteDialog(item)
                            },
                            onSelectVegeStatus = onSelectVegeStatus,
                            onSelectMoveFolder = onSelectMoveFolder
                        )
                    }
                }
            }
        }
    }
    when (uiState.openAddDialogType) {
        AddDialogType.AddVegeItem -> {
            AddTextCategoryDialog(
                titleResId = R.string.add_vege_item_dialog_title,
                selectCategory = uiState.selectCategory,
                inputText = uiState.inputText,
                isAddAble = uiState.isAddAble,
                onValueChange = changeInputText,
                onConfirmClick = { onAddDialogConfirmClick(AddDialogType.AddVegeItem) },
                onCancelClick = onDismiss,
                onSelectVegeCategory = onSelectVegeCategory,
            )
        }

        else -> {}
    }

    if (uiState.isOpenDeleteDialog) {
        ConfirmDeleteItemDialog(
            deleteItem = uiState.selectedItem,
            onDismissRequest = {
                closeDeleteDialog()
            },
            onConfirmClick = {
                confirmItemDelete()
                closeDeleteDialog()
            },
            onCancelClick = {
                closeDeleteDialog()
            }
        )
    }
    if (uiState.isOpenFolderMoveBottomSheet) {
        FolderMoveBottomSheet(
            folders = vegetablesState.vegetableFolders,
            sheetState = bottomSheetState,
            onDismissRequest = closeFolderBottomSheet,
            onFolderItemClick = {
                val selectedItem = uiState.selectedItem ?: return@FolderMoveBottomSheet
                onFolderItemClick(
                    VegeItem(
                        id = selectedItem.id,
                        name = selectedItem.name,
                        uuid = selectedItem.uuid,
                        status = selectedItem.status,
                        category = selectedItem.category,
                        folderId = it
                    )
                )
                scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                    if (!bottomSheetState.isVisible) {
                        closeFolderBottomSheet()
                    }
                }
            },
            onCancelClick = {
                scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                    if (!bottomSheetState.isVisible) {
                        closeFolderBottomSheet()
                    }
                }
            }
        )
    }
}

@DarkLightPreview
@Composable
fun FolderScreenPreview(
    @PreviewParameter(FolderPreviewParameterProvider::class) params: FolderPreviewParameterProvider.Params
) {
    VegegrowthAppTheme {
        FolderScreen(
            uiState = params.uiState,
            vegetablesState = params.vegetablesState,
            onBackNavigationButtonClick = {},
            onSelectVegeStatus = {},
            openDeleteDialog = {},
            onFilterItemClick = {},
            onEditIconClick = {},
            onDeleteVegeItem = {},
            onSelectVegeCategory = {},
            closeDeleteDialog = {},
            confirmItemDelete = {},
            onCancelMenuClick = {},
            openAddDialogType = {},
            changeInputText = {},
            onVegeItemClick = {},
            onDismiss = {},
            onAddDialogConfirmClick = {},
            onFolderMoveIconClick = {},
            onSelectMoveFolder = {},
            onFolderItemClick = {},
            closeFolderBottomSheet = {}
        )
    }
}

class FolderPreviewParameterProvider : PreviewParameterProvider<FolderPreviewParameterProvider.Params> {
    override val values: Sequence<Params> = sequenceOf(
        Params(
            uiState = FolderScreenUiState.initial(),
            vegetablesState = VegetablesState.initial().copy(
                vegetables = HomeScreenDummy.vegeList(),
                vegetableDetails = ManageScreenDummy.getVegetableDetailList(),
            )
        )
    )

    data class Params(
        val uiState: FolderScreenUiState,
        val vegetablesState: VegetablesState,
    )
}
