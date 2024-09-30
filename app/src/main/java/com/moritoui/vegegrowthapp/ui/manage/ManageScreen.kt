package com.moritoui.vegegrowthapp.ui.manage

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.moritoui.vegegrowthapp.R
import com.moritoui.vegegrowthapp.dummies.ManageScreenDummy
import com.moritoui.vegegrowthapp.model.VegeItemDetail
import com.moritoui.vegegrowthapp.navigation.Screen
import com.moritoui.vegegrowthapp.ui.analytics.SendScreenEvent
import com.moritoui.vegegrowthapp.ui.manage.model.ManageScreenUiState
import com.moritoui.vegegrowthapp.ui.manage.view.DeleteRegisteredItemDialog
import com.moritoui.vegegrowthapp.ui.manage.view.DrawLineChart
import com.moritoui.vegegrowthapp.ui.manage.view.ImageBottomSheet
import com.moritoui.vegegrowthapp.ui.manage.view.ManageLoading
import com.moritoui.vegegrowthapp.ui.manage.view.MemoEditorDialog
import com.moritoui.vegegrowthapp.ui.navigation.NavigationAppTopBar
import com.moritoui.vegegrowthapp.ui.theme.VegegrowthAppTheme
import kotlinx.coroutines.launch

@Composable
fun ManageScreen(navController: NavController, viewModel: ManageScreenViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ManageScreen(
        uiState = uiState,
        onBackNavigationButtonClick = navController::popBackStack,
        onImageClick = viewModel::changeOpenImageBottomSheet,
        imagePathList = uiState.vegeRepositoryList.map { it.imagePath },
        onEditClick = viewModel::changeOpenMemoEditorBottomSheet,
        onDismissRequest = viewModel::changeOpenImageBottomSheet,
        onMemoTextChange = viewModel::changeMemoText,
        onCancelButtonClick = viewModel::cancelEditMemo,
        onSaveButtonClick = viewModel::saveEditMemo,
        onDismissDeleteDialog = viewModel::deleteDialogClose,
        onDeleteDialogConfirmClick = viewModel::deleteVegeItemDetail,
        onDeleteButtonClick = viewModel::onDeleteItemButtonClick
    )

    SendScreenEvent(screen = Screen.ManageScreen)
}

@Composable
private fun ManageScreen(
    uiState: ManageScreenUiState,
    onBackNavigationButtonClick: () -> Unit,
    onImageClick: () -> Unit,
    imagePathList: List<String>,
    onEditClick: (Int) -> Unit,
    onDismissRequest: () -> Unit,
    onMemoTextChange: (String) -> Unit,
    onCancelButtonClick: () -> Unit,
    onSaveButtonClick: (VegeItemDetail) -> Unit,
    onDismissDeleteDialog: () -> Unit,
    onDeleteDialogConfirmClick: () -> Unit,
    onDeleteButtonClick: (VegeItemDetail) -> Unit,
) {
    var isLoaded = rememberSaveable {
        mutableStateOf(false)
    }
    Scaffold(
        topBar = {
            NavigationAppTopBar(
                title = stringResource(R.string.manage_screen_title),
                onBackNavigationButtonClick = onBackNavigationButtonClick
            )
        }
    ) { it ->
        if (uiState.vegeRepositoryList.isEmpty()) {
            ManageLoading(
                modifier = Modifier.padding(top = it.calculateTopPadding()),
                isLoaded = isLoaded.value,
                onBackClick = onBackNavigationButtonClick
            )
            return@Scaffold
        }

        LaunchedEffect(Unit) {
            isLoaded.value = true
        }

        val pagerState = rememberPagerState(
            initialPage = uiState.vegeRepositoryList.lastIndex,
            pageCount = { uiState.pagerCount }
        )

        val scope = rememberCoroutineScope()

        val selectedVegeItemDetail = uiState.vegeRepositoryList.getOrNull(pagerState.currentPage)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = it.calculateTopPadding()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(horizontal = 16.dp),
                onClick = {
                    onDeleteButtonClick(uiState.vegeRepositoryList[pagerState.currentPage])
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_delete),
                    contentDescription = stringResource(id = R.string.description_delete_button_icon),
                    tint = MaterialTheme.colorScheme.error
                )
            }
            DrawLineChart(
                currentIndex = pagerState.currentPage,
                data = uiState.vegeRepositoryList,
                modifier = Modifier
                    .weight(1f)
            )

            ImageCarousel(
                pagerCount = uiState.pagerCount,
                pagerState = pagerState,
                modifier = Modifier.weight(1f),
                currentImageBarHeight = 5,
                onImageClick = onImageClick,
                // ボトムバークリックでも画像遷移できるように -> Coroutineが必要
                onImageBottomBarClick = { scope.launch { pagerState.animateScrollToPage(it) } },
                currentImageBarModifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 72.dp, top = 12.dp, end = 72.dp, bottom = 8.dp),
                imagePathList = imagePathList
            )
            DetailData(
                memoData = uiState.vegeRepositoryList[pagerState.currentPage].memo,
                onEditClick = { onEditClick(pagerState.currentPage) },
                modifier = Modifier.height((LocalConfiguration.current.screenHeightDp / 6).dp)
            )
        }

        if (uiState.isOpenImageBottomSheet) {
            ImageBottomSheet(
                pagerCount = uiState.pagerCount,
                imageFilePathList = imagePathList,
                onDismissRequest = {
                    scope.launch { pagerState.animateScrollToPage(it) }
                    onDismissRequest()
                },
                index = pagerState.currentPage
            )
        }

        if (uiState.isOpenMemoEditorBottomSheet && selectedVegeItemDetail != null) {
            MemoEditorDialog(
                inputText = uiState.inputMemoText,
                onValueChange = onMemoTextChange,
                onCancelButtonClick = onCancelButtonClick,
                onSaveButtonClick = { onSaveButtonClick(selectedVegeItemDetail) },
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (uiState.isOpenDeleteDialog) {
            DeleteRegisteredItemDialog(
                onDismissDeleteDialog = onDismissDeleteDialog,
                onDeleteDialogConfirmClick = {
                    if (uiState.vegeRepositoryList.lastIndex == pagerState.currentPage) {
                        scope.launch {
                            pagerState.scrollToPage(uiState.vegeRepositoryList.lastIndex - 1)
                            onDeleteDialogConfirmClick()
                        }
                    } else {
                        onDeleteDialogConfirmClick()
                    }
                }
            )
        }
    }
}

@Composable
fun ImageCarousel(
    pagerCount: Int,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    currentImageBarHeight: Int,
    onImageClick: () -> Unit,
    onImageBottomBarClick: (Int) -> Unit,
    currentImageBarModifier: Modifier = Modifier,
    imagePathList: List<String>,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        HorizontalPager(
            modifier =
            Modifier
                .weight(1f),
            state = pagerState,
            contentPadding = PaddingValues(start = 24.dp, top = 12.dp, end = 24.dp),
            pageSpacing = 8.dp
        ) { page ->
            Box(
                modifier =
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.primaryContainer.copy(0.1f))
                    .border(
                        width = 4.dp,
                        color = MaterialTheme.colorScheme.onSurface.copy(0.05f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = imagePathList[page],
                    contentDescription = null,
                    modifier =
                    Modifier
                        .aspectRatio(1f / 1f)
                        .clickable { onImageClick() }
                        .padding(8.dp),
                    error = painterResource(id = R.drawable.no_image),
                    // Previewで見えるようにするため
                    placeholder = painterResource(R.drawable.loading_image)
                )
            }
        }
        Row(
            modifier =
            currentImageBarModifier
                .height(currentImageBarHeight.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pagerCount) {
                val backGroundColor =
                    when (it) {
                        pagerState.currentPage -> Color.Blue
                        else -> Color.LightGray
                    }
                Box(
                    // タップで画面遷移できるようにする
                    modifier = Modifier
                        .weight(1f)
                        .height(currentImageBarHeight.dp)
                        .background(backGroundColor)
                        .clickable(onClick = { onImageBottomBarClick(it) })
                )
            }
        }
    }
}

@Composable
fun DetailData(memoData: String, onEditClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .padding(start = 24.dp, end = 24.dp)
    ) {
        MemoData(
            memoData = memoData,
            onEditClick = onEditClick,
            modifier = Modifier
        )
    }
}

@Composable
fun MemoData(memoData: String, onEditClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.onSurface.copy(0.05f))
    ) {
        MemoTopBar(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.onSurface.copy(0.05f))
                .padding(start = 12.dp, end = 12.dp),
            onEditClick = { onEditClick() }
        )
        LazyColumn(
            modifier = Modifier
                .padding(start = 12.dp, end = 12.dp)
        ) {
            item {
                Text(
                    text = memoData,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    minLines = 3
                )
            }
        }
    }
}

@Composable
fun MemoTopBar(modifier: Modifier = Modifier, onEditClick: () -> Unit) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "メモ"
        )
        Spacer(Modifier.weight(1f))
        TextButton(
            modifier = Modifier.width(96.dp),
            onClick = { onEditClick() }
        ) {
            Row(
                modifier = Modifier.padding(4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    painterResource(id = R.drawable.ic_edit),
                    contentDescription = "メモ編集"
                )
                Text(
                    text = "編集"
                )
            }
        }
    }
}

@Composable
@PreviewLightDark
fun ManageScreenPreview(@PreviewParameter(ManageScreenPreviewParameterProvider::class) params: ManageScreenPreviewParameterProvider.Params) {
    VegegrowthAppTheme {
        ManageScreen(
            uiState = params.uiState,
            onBackNavigationButtonClick = {},
            onImageClick = {},
            imagePathList = params.imagePathList,
            onEditClick = {},
            onDismissRequest = {},
            onMemoTextChange = {},
            onCancelButtonClick = {},
            onSaveButtonClick = {},
            onDismissDeleteDialog = {},
            onDeleteDialogConfirmClick = {},
            onDeleteButtonClick = {}
        )
    }
}

class ManageScreenPreviewParameterProvider : PreviewParameterProvider<ManageScreenPreviewParameterProvider.Params> {
    override val values: Sequence<Params> =
        sequenceOf(
            Params(
                uiState =
                ManageScreenUiState.initialState().copy(
                    vegeRepositoryList = ManageScreenDummy.getVegetableDetailList(),
                    pagerCount = ManageScreenDummy.getImagePathList().size
                ),
                imagePathList = ManageScreenDummy.getImagePathList()
            )
        )

    data class Params(val uiState: ManageScreenUiState, val imagePathList: List<String>)
}
