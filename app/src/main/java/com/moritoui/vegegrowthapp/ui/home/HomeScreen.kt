package com.moritoui.vegegrowthapp.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.moritoui.vegegrowthapp.R
import com.moritoui.vegegrowthapp.data.room.model.VegetableFolderEntity
import com.moritoui.vegegrowthapp.dummies.HomeScreenDummy
import com.moritoui.vegegrowthapp.dummies.ManageScreenDummy
import com.moritoui.vegegrowthapp.model.FilterStatus
import com.moritoui.vegegrowthapp.model.SelectMenu
import com.moritoui.vegegrowthapp.model.VegeCategory
import com.moritoui.vegegrowthapp.model.VegeItem
import com.moritoui.vegegrowthapp.navigation.HomeAddItem
import com.moritoui.vegegrowthapp.previews.DarkLightPreview
import com.moritoui.vegegrowthapp.ui.common.VegeGrowthLoading
import com.moritoui.vegegrowthapp.ui.common.bottomsheet.FolderMoveBottomSheet
import com.moritoui.vegegrowthapp.ui.common.drawer.VegeGrowthNavigationDrawer
import com.moritoui.vegegrowthapp.ui.folder.navigateToFolder
import com.moritoui.vegegrowthapp.ui.home.model.AddDialogType
import com.moritoui.vegegrowthapp.ui.home.model.HomeScreenUiState
import com.moritoui.vegegrowthapp.ui.home.model.VegetablesState
import com.moritoui.vegegrowthapp.ui.home.view.AddTextCategoryDialog
import com.moritoui.vegegrowthapp.ui.home.view.ConfirmDeleteItemDialog
import com.moritoui.vegegrowthapp.ui.home.view.ItemListTopBar
import com.moritoui.vegegrowthapp.ui.home.view.VegeList
import com.moritoui.vegegrowthapp.ui.manual.navigateToManual
import com.moritoui.vegegrowthapp.ui.navigation.NavigationAppTopBar
import com.moritoui.vegegrowthapp.ui.takepicture.navigateToTakePicture
import com.moritoui.vegegrowthapp.ui.theme.VegegrowthAppTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = hiltViewModel(),
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val vegetablesState by viewModel.vegetablesState.collectAsStateWithLifecycle()
    HomeScreen(
        uiState = uiState,
        vegetablesState = vegetablesState,
        onDeleteVegeItem = viewModel::changeDeleteMode,
        openAddDialogType = viewModel::openAddDialog,
        onCancelMenuClick = viewModel::onCancelMenuClick,
        onEditIconClick = viewModel::changeEditMode,
        onFolderMoveIconClick = viewModel::changeFolderMoveMode,
        onFilterItemClick = viewModel::setFilterItemList,
        confirmItemDelete = viewModel::deleteItem,
        onSelectVegeStatus = viewModel::selectStatus,
        onVegeItemClick = navController::navigateToTakePicture,
        changeInputText = viewModel::changeInputText,
        onAddDialogConfirmClick = viewModel::onAddDialogConfirm,
        onDismiss = viewModel::closeDialog,
        onSelectVegeCategory = viewModel::selectCategory,
        closeDeleteDialog = viewModel::closeDeleteDialog,
        insertErrorEvent = viewModel.insertVegetableFolderEvent,
        openDeleteDialog = viewModel::openDeleteVegeItemDialog,
        onDeleteFolderItem = viewModel::openDeleteFolderDialog,
        onFolderClick = navController::navigateToFolder,
        onSelectMoveFolder = viewModel::openFolderMoveBottomSheetState,
        closeFolderBottomSheet = viewModel::closeFolderMoveBottomSheetState,
        onFolderItemClick = viewModel::vegeItemMoveFolder,
        onManualClick = navController::navigateToManual
    )

    // 画面遷移で戻ったときに処理する
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.reloadVegetables()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    uiState: HomeScreenUiState,
    vegetablesState: VegetablesState,
    openAddDialogType: (AddDialogType) -> Unit,
    onCancelMenuClick: () -> Unit,
    onDeleteVegeItem: () -> Unit,
    onEditIconClick: () -> Unit,
    onSelectMoveFolder: (VegeItem) -> Unit,
    onFilterItemClick: (FilterStatus) -> Unit,
    confirmItemDelete: () -> Unit,
    onSelectVegeStatus: (VegeItem) -> Unit,
    onVegeItemClick: (Int) -> Unit,
    changeInputText: (String) -> Unit,
    closeDeleteDialog: () -> Unit,
    openDeleteDialog: (VegeItem) -> Unit,
    onDeleteFolderItem: (VegetableFolderEntity) -> Unit,
    onFolderMoveIconClick: () -> Unit,
    onAddDialogConfirmClick: (AddDialogType) -> Unit,
    onDismiss: () -> Unit,
    onSelectVegeCategory: (VegeCategory) -> Unit,
    insertErrorEvent: Flow<Boolean>,
    onFolderClick: (Int) -> Unit,
    closeFolderBottomSheet: () -> Unit,
    onFolderItemClick: (VegeItem) -> Unit,
    onManualClick: () -> Unit,
) {
    var selectMenuExpanded by rememberSaveable { mutableStateOf(false) }
    var filterMenuExpanded by rememberSaveable { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    VegeGrowthNavigationDrawer(
        drawerState = drawerState,
        onManualClick = {
            scope.launch {
                onManualClick()
                drawerState.apply {
                    close()
                }
            }
        },
        content = {
            Scaffold(
                topBar = {
                    NavigationAppTopBar(
                        title = stringResource(R.string.home_screen_title),
                        actions = {
                            HomeAddItem(
                                onFolderAddClick = { openAddDialogType(AddDialogType.AddFolder) },
                                onAddClick = { openAddDialogType(AddDialogType.AddVegeItem) }
                            )
                        },
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    scope.launch {
                                        drawerState.apply {
                                            open()
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_menu),
                                    contentDescription = stringResource(id = R.string.description_menu_icon)
                                )
                            }
                        }
                    )
                }
            ) { innerPadding ->
                if (uiState.isLoading) {
                    VegeGrowthLoading(
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(horizontal = 8.dp)
                ) {
                    ItemListTopBar(
                        modifier = Modifier.fillMaxWidth(),
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
                    VegeList(
                        modifier = Modifier,
                        folders = vegetablesState.vegetableFolders,
                        vegetables = vegetablesState.vegetables,
                        vegetableDetails = vegetablesState.vegetableDetails,
                        onFolderClick = onFolderClick,
                        openDeleteDialog = openDeleteDialog,
                        onDeleteFolderItem = onDeleteFolderItem,
                        onSelectVegeStatus = onSelectVegeStatus,
                        onVegeItemClick = onVegeItemClick,
                        onSelectMoveFolder = onSelectMoveFolder,
                        selectMenu = uiState.selectMenu
                    )
                }
            }
        }
    )
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
                onSelectVegeCategory = onSelectVegeCategory
            )
        }
        AddDialogType.AddFolder -> {
            AddTextCategoryDialog(
                titleResId = R.string.add_folder_dialog_title,
                selectCategory = uiState.selectCategory,
                inputText = uiState.inputText,
                isAddAble = uiState.isAddAble,
                onValueChange = changeInputText,
                onConfirmClick = { onAddDialogConfirmClick(AddDialogType.AddFolder) },
                onCancelClick = onDismiss,
                onSelectVegeCategory = onSelectVegeCategory,
                errorEvent = insertErrorEvent
            )
        }
        else -> {
        }
    }

    if (uiState.isOpenDeleteDialog) {
        ConfirmDeleteItemDialog(
            deleteItem = uiState.selectedItem,
            deleteFolder = uiState.selectedFolder,
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
fun HomeScreenPreview(@PreviewParameter(HomePreviewParameterProvider::class) params: HomePreviewParameterProvider.Params) {
    VegegrowthAppTheme {
        HomeScreen(
            uiState = params.uiState,
            vegetablesState = params.vegetablesState,
            openAddDialogType = {},
            onSelectVegeCategory = {},
            onCancelMenuClick = {},
            onDeleteVegeItem = {},
            onFilterItemClick = {},
            onDismiss = {},
            onSelectVegeStatus = {},
            confirmItemDelete = {},
            onAddDialogConfirmClick = {},
            changeInputText = {},
            onEditIconClick = {},
            onVegeItemClick = {},
            closeDeleteDialog = {},
            openDeleteDialog = {},
            insertErrorEvent = flow { },
            onFolderClick = {},
            onDeleteFolderItem = {},
            onFolderMoveIconClick = {},
            onSelectMoveFolder = {},
            closeFolderBottomSheet = {},
            onFolderItemClick = {},
            onManualClick = {}
        )
    }
}

class HomePreviewParameterProvider : PreviewParameterProvider<HomePreviewParameterProvider.Params> {
    override val values: Sequence<Params> =
        sequenceOf(
            Params(
                uiState = HomeScreenUiState.initialState(),
                vegetablesState = VegetablesState.initial().copy(
                    vegetables = HomeScreenDummy.vegeList(),
                    vegetableDetails = ManageScreenDummy.getVegetableDetailList(),
                    vegetableFolders = HomeScreenDummy.vegeFolderList()
                )
            ),
            Params(
                uiState = HomeScreenUiState.initialState().copy(
                    selectMenu = SelectMenu.Edit
                ),
                vegetablesState = VegetablesState.initial().copy(
                    vegetables = HomeScreenDummy.vegeList(),
                    vegetableDetails = ManageScreenDummy.getVegetableDetailList(),
                    vegetableFolders = HomeScreenDummy.vegeFolderList()
                )
            ),
            Params(
                uiState = HomeScreenUiState.initialState().copy(
                    selectMenu = SelectMenu.Delete
                ),
                vegetablesState = VegetablesState.initial().copy(
                    vegetables = HomeScreenDummy.vegeList(),
                    vegetableDetails = ManageScreenDummy.getVegetableDetailList(),
                    vegetableFolders = HomeScreenDummy.vegeFolderList()
                )
            )
        )

    data class Params(val uiState: HomeScreenUiState, val vegetablesState: VegetablesState)
}
