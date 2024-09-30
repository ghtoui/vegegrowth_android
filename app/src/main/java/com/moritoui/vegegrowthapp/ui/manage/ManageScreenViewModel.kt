package com.moritoui.vegegrowthapp.ui.manage

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moritoui.vegegrowthapp.model.VegeItemDetail
import com.moritoui.vegegrowthapp.model.toVegetableEntity
import com.moritoui.vegegrowthapp.repository.vegetabledetail.VegetableDetailRepository
import com.moritoui.vegegrowthapp.ui.manage.model.ManageScreenUiState
import com.moritoui.vegegrowthapp.usecases.DeleteVegeItemDetailUseCase
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
    private val getVegetableDetailsUseCase: GetVegetableDetailsUseCase,
    private val getImagePathListUseCase: GetImagePathListUseCase,
    private val detailRepository: VegetableDetailRepository,
    private val deleteVegeItemDetailUseCase: DeleteVegeItemDetailUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val args = checkNotNull(savedStateHandle.get<Int>("vegetableId"))
    private var vegeItemDetailList: List<VegeItemDetail> = emptyList()

    private val _uiState = MutableStateFlow(ManageScreenUiState.initialState())
    val uiState: StateFlow<ManageScreenUiState> = _uiState.asStateFlow()

    init {
        collectVegetableDetailsData()
    }

    private fun collectVegetableDetailsData() {
        viewModelScope.launch {
            getVegetableDetailsUseCase(args).collect { vegetableDetails ->
                _uiState.update {
                    it.copy(
                        pagerCount = vegetableDetails.size,
                        vegeRepositoryList = vegetableDetails
                    )
                }
            }
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
                isOpenMemoEditorBottomSheet = isOpenMemoEditorBottomSheet
            )
        }
    }

    fun changeOpenImageBottomSheet() {
        updateState(
            isOpenImageBottomSheet = !_uiState.value.isOpenImageBottomSheet
        )
    }

    fun cancelEditMemo() {
        updateState(
            inputMemoText = "",
            isOpenMemoEditorBottomSheet = false
        )
    }

    fun changeMemoText(inputText: String) {
        updateState(inputMemoText = inputText)
    }

    fun saveEditMemo(vegeItemDetail: VegeItemDetail) {
        viewModelScope.launch {
            detailRepository.editMemo(
                memo = _uiState.value.inputMemoText,
                vegeItemDetail = vegeItemDetail
            )
        }
        updateState(
            inputMemoText = "",
            isOpenMemoEditorBottomSheet = false
        )
    }

    fun changeOpenMemoEditorBottomSheet(index: Int) {
        if (!_uiState.value.isOpenMemoEditorBottomSheet) {
            updateState(inputMemoText = vegeItemDetailList[index].memo)
        }
        updateState(
            isOpenMemoEditorBottomSheet = !_uiState.value.isOpenMemoEditorBottomSheet
        )
    }

    fun onDeleteItemButtonClick(selectedVegeItemDetail: VegeItemDetail) {
        _uiState.update {
            it.copy(
                isOpenDeleteDialog = true,
                selectedDeleteVegeDetail = selectedVegeItemDetail
            )
        }
    }

    fun deleteVegeItemDetail() {
        val deleteVegeItemDetail = _uiState.value.selectedDeleteVegeDetail ?: return
        viewModelScope.launch {
            deleteVegeItemDetailUseCase(deleteVegeItemDetail.toVegetableEntity())
            deleteDialogClose()
        }
    }

    fun deleteDialogClose() {
        _uiState.update {
            it.copy(
                isOpenDeleteDialog = false,
                selectedDeleteVegeDetail = null
            )
        }
    }
}
