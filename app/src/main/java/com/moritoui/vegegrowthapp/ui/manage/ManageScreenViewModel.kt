package com.moritoui.vegegrowthapp.ui.manage

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moritoui.vegegrowthapp.model.VegeItemDetail
import com.moritoui.vegegrowthapp.repository.vegetabledetail.VegetableDetailRepository
import com.moritoui.vegegrowthapp.ui.manage.model.ManageScreenUiState
import com.moritoui.vegegrowthapp.usecases.GetImagePathListUseCase
import com.moritoui.vegegrowthapp.usecases.GetVegetableDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageScreenViewModel
    @Inject
    constructor(
        getVegetableDetailsUseCase: GetVegetableDetailsUseCase,
        getImagePathListUseCase: GetImagePathListUseCase,
        private val detailRepository: VegetableDetailRepository,
        savedStateHandle: SavedStateHandle,
    ) : ViewModel() {
        private val args = checkNotNull(savedStateHandle.get<Int>("vegetableId"))
        private var vegeItemDetailList: List<VegeItemDetail> = emptyList()

        var takePicFileList: List<String> = emptyList()

        private val _uiState = MutableStateFlow(ManageScreenUiState.initialState())
        val uiState: StateFlow<ManageScreenUiState> = _uiState.asStateFlow()

        init {
            viewModelScope.launch {
                vegeItemDetailList = getVegetableDetailsUseCase(args)
                takePicFileList = getImagePathListUseCase(args)
                updateState(
                    pagerCount = vegeItemDetailList.size,
                    vegeRepositoryList = vegeItemDetailList,
                )
            }
        }

        private fun updateState(
            pagerCount: Int = _uiState.value.pagerCount,
            vegeRepositoryList: List<VegeItemDetail> = _uiState.value.vegeRepositoryList,
            isOpenImageBottomSheet: Boolean = _uiState.value.isOpenImageBottomSheet,
            inputMemoText: String = _uiState.value.inputMemoText,
            isOpenMemoEditorBottomSheet: Boolean = _uiState.value.isOpenMemoEditorBottomSheet,
        ) {
            _uiState.update { currentState ->
                currentState.copy(
                    pagerCount = pagerCount,
                    vegeRepositoryList = vegeRepositoryList,
                    isOpenImageBottomSheet = isOpenImageBottomSheet,
                    inputMemoText = inputMemoText,
                    isOpenMemoEditorBottomSheet = isOpenMemoEditorBottomSheet,
                )
            }
        }

        fun changeOpenImageBottomSheet() {
            updateState(
                isOpenImageBottomSheet = !_uiState.value.isOpenImageBottomSheet,
            )
        }

        fun cancelEditMemo() {
            updateState(
                inputMemoText = "",
                isOpenMemoEditorBottomSheet = false,
            )
        }

        fun changeMemoText(inputText: String) {
            updateState(inputMemoText = inputText)
        }

        fun saveEditMemo(vegeItemDetail: VegeItemDetail) {
            viewModelScope.launch {
                detailRepository.editMemo(
                    memo = _uiState.value.inputMemoText,
                    vegeItemDetail = vegeItemDetail,
                )
            }
            updateState(
                inputMemoText = "",
                isOpenMemoEditorBottomSheet = false,
            )
        }

        fun changeOpenMemoEditorBottomSheet(index: Int) {
            if (!_uiState.value.isOpenMemoEditorBottomSheet) {
                updateState(inputMemoText = vegeItemDetailList[index].memo)
            }
            updateState(
                isOpenMemoEditorBottomSheet = !_uiState.value.isOpenMemoEditorBottomSheet,
            )
        }
    }
