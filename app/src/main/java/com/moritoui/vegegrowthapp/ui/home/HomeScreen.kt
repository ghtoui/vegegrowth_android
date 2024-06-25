package com.moritoui.vegegrowthapp.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.moritoui.vegegrowthapp.R
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
import com.moritoui.vegegrowthapp.ui.home.model.HomeScreenUiState
import com.moritoui.vegegrowthapp.ui.home.model.HomeVegetablesState
import com.moritoui.vegegrowthapp.ui.home.view.AddAlertWindow
import com.moritoui.vegegrowthapp.ui.home.view.ConfirmDeleteItemDialog
import com.moritoui.vegegrowthapp.ui.home.view.ItemListTopBar
import com.moritoui.vegegrowthapp.ui.home.view.VegeItemListCard
import com.moritoui.vegegrowthapp.ui.takepicture.navigateToTakePicture
import com.moritoui.vegegrowthapp.ui.theme.VegegrowthAppTheme

@Composable
fun HomeScreen(viewModel: HomeScreenViewModel = hiltViewModel(), navController: NavController) {
    val uiState by viewModel.uiState.collectAsState()
    val vegetablesState by viewModel.vegetablesState.collectAsState()
    HomeScreen(
        uiState = uiState,
        vegetablesState = vegetablesState,
        openAddVegeItemDialog = viewModel::openAddDialog,
        onCancelMenuClick = viewModel::onCancelMenuClick,
        onDeleteItem = viewModel::changeDeleteMode,
        onEditIconClick = viewModel::changeEditMode,
        onFilterItemClick = viewModel::setFilterItemList,
        confirmItemDelete = viewModel::deleteItem,
        onSelectVegeStatus = viewModel::selectStatus,
        onVegeItemClick = {
            navController.navigateToTakePicture(it)
        },
        changeInputText = viewModel::changeInputText,
        onConfirmClick = viewModel::saveVegeItem,
        onDismiss = viewModel::closeDialog,
        onSelectVegeCategory = viewModel::selectCategory,
        closeDeleteDialog = viewModel::closeDeleteDialog,
        openDeleteDialog = viewModel::openDeleteDialog
    )
}

@Composable
private fun HomeScreen(
    uiState: HomeScreenUiState,
    vegetablesState: HomeVegetablesState,
    openAddVegeItemDialog: () -> Unit,
    onCancelMenuClick: () -> Unit,
    onDeleteItem: () -> Unit,
    onEditIconClick: () -> Unit,
    onFilterItemClick: (FilterStatus) -> Unit,
    confirmItemDelete: () -> Unit,
    onSelectVegeStatus: (VegeItem) -> Unit,
    onVegeItemClick: (Int) -> Unit,
    changeInputText: (String) -> Unit,
    closeDeleteDialog: () -> Unit,
    openDeleteDialog: (VegeItem) -> Unit,
    onConfirmClick: () -> Unit,
    onDismiss: () -> Unit,
    onSelectVegeCategory: (VegeCategory) -> Unit
) {
    var selectMenuExpanded by rememberSaveable { mutableStateOf(false) }
    var filterMenuExpanded by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            NavigationAppTopBar(
                title = stringResource(R.string.first_screen_title),
                actions = { HomeAddItem(onAddClick = openAddVegeItemDialog) },
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
                        modifier = Modifier
                            .padding(top = 16.dp),
                        onCancelClick = onCancelMenuClick,
                        onDeleteIconClick = onDeleteItem,
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
                            .fillMaxSize(),
                    )
                }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = it.calculateTopPadding())
                ) {
                    items(vegetablesState.vegetables.zip(vegetablesState.vegetableDetails), key = { item -> item.first.id }) { vegetable ->
                        VegeItemListCard(
                            vegetable = vegetable.first,
                            vegetableDetail = vegetable.second,
                            onVegeItemClick = { onVegeItemClick(it) },
                            selectMenu = uiState.selectMenu,
                            onItemDeleteClick = { item ->
                                openDeleteDialog(item)
                            },
                            onSelectVegeStatus = onSelectVegeStatus,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
    AddAlertWindow(
        selectCategory = uiState.selectCategory,
        isOpenDialog = uiState.isOpenAddDialog,
        inputText = uiState.inputText,
        isAddAble = uiState.isAddAble,
        onValueChange = changeInputText,
        onConfirmClick = onConfirmClick,
        onDismissClick = onDismiss,
        onSelectVegeCategory = onSelectVegeCategory
    )
    if (uiState.isOpenDeleteDialog) {
        ConfirmDeleteItemDialog(
            deleteItem = uiState.targetDeleteItem,
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
            openAddVegeItemDialog = {},
            onSelectVegeCategory = {},
            onCancelMenuClick = {},
            onDeleteItem = {},
            onFilterItemClick = {},
            onDismiss = {},
            onSelectVegeStatus = {},
            confirmItemDelete = {},
            onConfirmClick = {},
            changeInputText = {},
            onEditIconClick = {},
            onVegeItemClick = {},
            closeDeleteDialog = {},
            openDeleteDialog = {}
        )
    }
}

class HomePreviewParameterProvider : PreviewParameterProvider<HomePreviewParameterProvider.Params> {
    override val values: Sequence<HomePreviewParameterProvider.Params> =
        sequenceOf(
            Params(
                uiState = HomeScreenUiState.initialState(),
                vegetablesState = HomeVegetablesState.initial().copy(
                    vegetables = HomeScreenDummy.vegeList(),
                    vegetableDetails = ManageScreenDummy.getVegetableDetailList()
                )
            ),
            Params(
                uiState = HomeScreenUiState.initialState().copy(
                    selectMenu = SelectMenu.Edit,
                ),
                vegetablesState = HomeVegetablesState.initial().copy(
                    vegetables = HomeScreenDummy.vegeList(),
                    vegetableDetails = ManageScreenDummy.getVegetableDetailList()
                )
            ),
            Params(
                uiState = HomeScreenUiState.initialState().copy(
                    selectMenu = SelectMenu.Delete
                ),
                vegetablesState = HomeVegetablesState.initial().copy(
                    vegetables = HomeScreenDummy.vegeList(),
                    vegetableDetails = ManageScreenDummy.getVegetableDetailList()
                )
            )
        )

    data class Params(
        val uiState: HomeScreenUiState,
        val vegetablesState: HomeVegetablesState,
    )
}
