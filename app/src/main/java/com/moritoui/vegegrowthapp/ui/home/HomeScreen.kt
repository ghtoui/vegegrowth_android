package com.moritoui.vegegrowthapp.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.moritoui.vegegrowthapp.R
import com.moritoui.vegegrowthapp.dummies.HomeScreenDummy
import com.moritoui.vegegrowthapp.model.FilterStatus
import com.moritoui.vegegrowthapp.model.SelectMenu
import com.moritoui.vegegrowthapp.model.VegeCategory
import com.moritoui.vegegrowthapp.model.VegeItem
import com.moritoui.vegegrowthapp.model.VegeStatus
import com.moritoui.vegegrowthapp.model.VegeStatusMethod
import com.moritoui.vegegrowthapp.model.getIcon
import com.moritoui.vegegrowthapp.model.getTint
import com.moritoui.vegegrowthapp.navigation.HomeAddItem
import com.moritoui.vegegrowthapp.navigation.NavigationAppTopBar
import com.moritoui.vegegrowthapp.previews.DarkLightPreview
import com.moritoui.vegegrowthapp.ui.home.model.HomeScreenUiState
import com.moritoui.vegegrowthapp.ui.home.model.HomeVegetablesState
import com.moritoui.vegegrowthapp.ui.home.view.AddAlertWindow
import com.moritoui.vegegrowthapp.ui.home.view.ConfirmDeleteItemDialog
import com.moritoui.vegegrowthapp.ui.home.view.ItemListDropDownMenuItem
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
        Box(modifier = Modifier.padding(it)) {
            Scaffold(
                topBar = {
                    ItemListTopBar(
                        modifier = Modifier
                            .padding(start = 24.dp, top = 16.dp, end = 8.dp),
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
                LazyColumn(
                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(it)
                        .padding(horizontal = 24.dp)
                ) {
                    items(vegetablesState.vegetables.zip(vegetablesState.vegetableDetails), key = { item -> item.first.id }) { vegetable ->
                        VegeItemListCard(
                            vegetable = vegetable.first,
                            vegetableDetail = vegetable.second,
                            onClick = { onVegeItemClick(it) }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
//                        VegeItemElement(
//                            item = item,
//                            selectMenu = uiState.selectMenu,
//                            onItemDeleteClick = { item ->
//                                openDeleteDialog(item)
//                            },
//                            onSelectVegeStatus = onSelectVegeStatus,
//                            onVegeItemClick = { onVegeItemClick(it.id) }
//                        )
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

@Composable
fun VegeItemElement(
    modifier: Modifier = Modifier,
    item: VegeItem,
    onItemDeleteClick: (VegeItem) -> Unit,
    selectMenu: SelectMenu,
    onVegeItemClick: (VegeItem) -> Unit = { },
    onSelectVegeStatus: (VegeItem) -> Unit
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    var isCheck by rememberSaveable { mutableStateOf(false) }
    var showStatus by rememberSaveable { mutableStateOf(item.status) }
    showStatus = item.status

    val statusIcon: ImageVector = VegeStatusMethod.getIcon(showStatus)
    val statusIconTint: Color = VegeStatusMethod.getIconTint(vegeStatus = showStatus) ?: LocalContentColor.current
    val iconTint: Color = item.category.getTint(otherColor = LocalContentColor.current)
    val categoryIcon = item.category.getIcon()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .clickable(onClick = { onVegeItemClick(item) }),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (categoryIcon != null) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = categoryIcon),
                tint = iconTint,
                contentDescription = null
            )
        } else {
            Spacer(modifier = Modifier.width(24.dp))
        }
        Text(
            text = item.name,
            modifier =
            Modifier
                .padding(start = 16.dp, end = 24.dp)
        )
        Icon(
            statusIcon,
            contentDescription = null,
            tint = statusIconTint
        )
        if (selectMenu != SelectMenu.None) {
            Box(
                modifier =
                Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.BottomEnd
            ) {
                IconButton(onClick = {
                    onItemDeleteClick(item)
                    isCheck = !isCheck
                }) {
                    if (selectMenu == SelectMenu.Delete) {
                        Icon(
                            Icons.Filled.Delete,
                            contentDescription = stringResource(id = R.string.delete_text),
                            tint =
                            when (isCheck) {
                                false -> Color.Red
                                true -> Color.Transparent
                            },
                            modifier = Modifier.aspectRatio(1f / 1f)
                        )
                    } else {
                        IconButton(onClick = { expanded = true }) {
                            Icon(
                                Icons.Filled.Edit,
                                contentDescription = stringResource(id = R.string.delete_text),
                                modifier = Modifier.aspectRatio(1f / 1f)
                            )
                        }
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = {
                            expanded = false
                        }
                    ) {
                        VegeStatus.values().forEach { status ->
                            ItemListDropDownMenuItem(
                                icon = VegeStatusMethod.getIcon(status),
                                text = stringResource(VegeStatusMethod.getText(status)),
                                iconTint =
                                VegeStatusMethod.getIconTint(status)
                                    ?: LocalContentColor.current,
                                onClick = {
                                    showStatus = status
                                    item.status = status
                                    onSelectVegeStatus(item)
                                    expanded = false
                                }
                            )
                        }
                    }
                    Icon(
                        Icons.Filled.Check,
                        contentDescription = stringResource(R.string.done_text),
                        tint =
                        when (isCheck) {
                            false -> Color.Transparent
                            true -> Color.Black
                        },
                        modifier = Modifier.aspectRatio(1f / 1f)
                    )
                }
            }
        } else {
            isCheck = false
        }
    }
    Box(
        modifier =
        Modifier
            .fillMaxWidth()
            .background(Color.LightGray)
            .height(0.5.dp)
    )
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
                    vegetables = HomeScreenDummy.vegeList()
                )
            ),
            Params(
                uiState = HomeScreenUiState.initialState().copy(
                    selectMenu = SelectMenu.Edit
                ),
                vegetablesState = HomeVegetablesState.initial().copy(
                    vegetables = HomeScreenDummy.vegeList()
                )
            ),
            Params(
                uiState = HomeScreenUiState.initialState().copy(
                    selectMenu = SelectMenu.Delete
                ),
                vegetablesState = HomeVegetablesState.initial().copy(
                    vegetables = HomeScreenDummy.vegeList()
                )
            )
        )

    data class Params(
        val uiState: HomeScreenUiState,
        val vegetablesState: HomeVegetablesState,
    )
}
