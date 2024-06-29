package com.moritoui.vegegrowthapp.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
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
import com.moritoui.vegegrowthapp.navigation.NavigationAppTopBar
import com.moritoui.vegegrowthapp.previews.DarkLightPreview
import com.moritoui.vegegrowthapp.ui.common.VegeGrowthLoading
import com.moritoui.vegegrowthapp.ui.home.model.AddDialogType
import com.moritoui.vegegrowthapp.ui.home.model.HomeScreenUiState
import com.moritoui.vegegrowthapp.ui.home.model.HomeVegetablesState
import com.moritoui.vegegrowthapp.ui.home.view.AddTextCategoryDialog
import com.moritoui.vegegrowthapp.ui.home.view.ConfirmDeleteItemDialog
import com.moritoui.vegegrowthapp.ui.home.view.ItemListTopBar
import com.moritoui.vegegrowthapp.ui.home.view.VegeFolderCard
import com.moritoui.vegegrowthapp.ui.home.view.VegeItemListCard
import com.moritoui.vegegrowthapp.ui.takepicture.navigateToTakePicture
import com.moritoui.vegegrowthapp.ui.theme.VegegrowthAppTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@Composable
fun HomeScreen(viewModel: HomeScreenViewModel = hiltViewModel(), navController: NavController) {
    val uiState by viewModel.uiState.collectAsState()
    val vegetablesState by viewModel.vegetablesState.collectAsState()
    HomeScreen(
        uiState = uiState,
        vegetablesState = vegetablesState,
        onDeleteVegeItem = viewModel::changeDeleteMode,
        openAddDialogType = viewModel::openAddDialog,
        onCancelMenuClick = viewModel::onCancelMenuClick,
        onEditIconClick = viewModel::changeEditMode,
        onFilterItemClick = viewModel::setFilterItemList,
        confirmItemDelete = viewModel::deleteItem,
        onSelectVegeStatus = viewModel::selectStatus,
        onVegeItemClick = {
            navController.navigateToTakePicture(it)
        },
        changeInputText = viewModel::changeInputText,
        onAddDialogConfirmClick = viewModel::onAddDialogConfirm,
        onDismiss = viewModel::closeDialog,
        onSelectVegeCategory = viewModel::selectCategory,
        closeDeleteDialog = viewModel::closeDeleteDialog,
        insertErrorEvent = viewModel.insertVegetableFolderEvent,
        openDeleteDialog = viewModel::openDeleteVegeItemDialog,
        onDeleteFolderItem = viewModel::openDeleteFolderDialog,
        onFolderClick = {},
    )
}

@Composable
private fun HomeScreen(
    uiState: HomeScreenUiState,
    vegetablesState: HomeVegetablesState,
    openAddDialogType: (AddDialogType) -> Unit,
    onCancelMenuClick: () -> Unit,
    onDeleteVegeItem: () -> Unit,
    onEditIconClick: () -> Unit,
    onFilterItemClick: (FilterStatus) -> Unit,
    confirmItemDelete: () -> Unit,
    onSelectVegeStatus: (VegeItem) -> Unit,
    onVegeItemClick: (Int) -> Unit,
    changeInputText: (String) -> Unit,
    closeDeleteDialog: () -> Unit,
    openDeleteDialog: (VegeItem) -> Unit,
    onDeleteFolderItem: (VegetableFolderEntity) -> Unit,
    onAddDialogConfirmClick: (AddDialogType) -> Unit,
    onDismiss: () -> Unit,
    onSelectVegeCategory: (VegeCategory) -> Unit,
    insertErrorEvent: Flow<Boolean>,
    onFolderClick: (VegetableFolderEntity) -> Unit,
) {
    var selectMenuExpanded by rememberSaveable { mutableStateOf(false) }
    var filterMenuExpanded by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            NavigationAppTopBar(
                title = stringResource(R.string.first_screen_title),
                actions = { HomeAddItem(
                    onFolderAddClick = { openAddDialogType(AddDialogType.AddFolder) },
                    onAddClick = { openAddDialogType(AddDialogType.AddVegeItem) }
                ) },
                isVisibleBackButton = false
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
                    LazyVerticalGrid(
                        modifier = Modifier
                            .padding(top = it.calculateTopPadding(), bottom = 8.dp),
                        columns = GridCells.Fixed(2),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        if (vegetablesState.vegetableFolders.isNotEmpty()) {
                            item(
                                span = { GridItemSpan(maxLineSpan) },
                            ) {
                                Column {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_folder),
                                            contentDescription = null,
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(stringResource(id = R.string.home_folder))
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))
                                    HorizontalDivider(
                                        modifier = Modifier.fillMaxWidth(),
                                        thickness = 3.dp
                                    )
                                }
                            }
                        }
                        items(vegetablesState.vegetableFolders) { folder ->
                            VegeFolderCard(
                                vegetableFolder = folder,
                                onFolderClick = { onFolderClick(folder) },
                                selectMenu = uiState.selectMenu,
                                onItemDeleteClick = { onDeleteFolderItem(it) }
                            )
                        }
                        if (vegetablesState.vegetables.isNotEmpty()) {
                            item(
                                span = { GridItemSpan(maxLineSpan) },
                            ) {
                                Column {
                                    Text(stringResource(id = R.string.home_item_not_classified))
                                    Spacer(modifier = Modifier.height(2.dp))
                                    HorizontalDivider(
                                        modifier = Modifier.fillMaxWidth(),
                                        thickness = 3.dp
                                    )
                                }
                            }
                        }
                        vegetablesState.vegetables.zip(vegetablesState.vegetableDetails).forEach { vegetable ->
                            item(
                                key = "${vegetable.first.id}, ${vegetable.first.name}",
                                span = { GridItemSpan(maxLineSpan) },
                            ) {
                                VegeItemListCard(
                                    vegetable = vegetable.first,
                                    vegetableDetail = vegetable.second,
                                    onVegeItemClick = { onVegeItemClick(it) },
                                    selectMenu = uiState.selectMenu,
                                    onItemDeleteClick = { item ->
                                        openDeleteDialog(item)
                                    },
                                    onSelectVegeStatus = onSelectVegeStatus
                                )
                            }
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
                errorEvent = insertErrorEvent,
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
                errorEvent = insertErrorEvent,
            )
        }
        else -> {

        }
    }
    if (uiState.isOpenDeleteDialog) {
        ConfirmDeleteItemDialog(
            deleteItem = uiState.targetDeleteItem,
            deleteFolder = uiState.targetDeleteFolder,
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
            onDeleteFolderItem = {}
        )
    }
}

class HomePreviewParameterProvider : PreviewParameterProvider<HomePreviewParameterProvider.Params> {
    override val values: Sequence<Params> =
        sequenceOf(
            Params(
                uiState = HomeScreenUiState.initialState(),
                vegetablesState = HomeVegetablesState.initial().copy(
                    vegetables = HomeScreenDummy.vegeList(),
                    vegetableDetails = ManageScreenDummy.getVegetableDetailList(),
                    vegetableFolders = HomeScreenDummy.vegeFolderList(),
                )
            ),
            Params(
                uiState = HomeScreenUiState.initialState().copy(
                    selectMenu = SelectMenu.Edit
                ),
                vegetablesState = HomeVegetablesState.initial().copy(
                    vegetables = HomeScreenDummy.vegeList(),
                    vegetableDetails = ManageScreenDummy.getVegetableDetailList(),
                    vegetableFolders = HomeScreenDummy.vegeFolderList(),
                )
            ),
            Params(
                uiState = HomeScreenUiState.initialState().copy(
                    selectMenu = SelectMenu.Delete
                ),
                vegetablesState = HomeVegetablesState.initial().copy(
                    vegetables = HomeScreenDummy.vegeList(),
                    vegetableDetails = ManageScreenDummy.getVegetableDetailList(),
                    vegetableFolders = HomeScreenDummy.vegeFolderList(),
                )
            )
        )

    data class Params(val uiState: HomeScreenUiState, val vegetablesState: HomeVegetablesState)
}
