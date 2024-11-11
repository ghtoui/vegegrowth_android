package com.moritoui.vegegrowthapp.ui.timeline.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moritoui.vegegrowthapp.model.VegeItem
import com.moritoui.vegegrowthapp.model.VegeItemData
import com.moritoui.vegegrowthapp.repository.timeline.TimelineRepository
import com.moritoui.vegegrowthapp.ui.timeline.home.model.TimelineHomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimelineHomeViewModel @Inject constructor(
    private val timelineRepository: TimelineRepository
) : ViewModel() {
    private var page: Int = 0
    private val loadState: MutableStateFlow<PagingListState> = MutableStateFlow(
        PagingListState.Initial
    )
    private val vegetables: MutableStateFlow<List<VegeItem>> = MutableStateFlow<List<VegeItem>>(
        emptyList()
    ).apply {
        viewModelScope.launch {
            load(isInitialLoad = true) {
                timelineRepository.getVegetables(0)
            }
        }
    }

    val uiState: StateFlow<TimelineHomeState> = combine(
        vegetables,
        loadState,
        ::TimelineHomeState
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TimelineHomeState.initial()
    )

    fun reload() {
        load(isInitialLoad = page == 0) {
            timelineRepository.getVegetables(page)
        }
    }

    fun pageAdd() {
        Log.d("test", "add")
        load {
            timelineRepository.getVegetables(page + 1)
        }
    }

    private fun load(
        isInitialLoad: Boolean = false,
        load: suspend() -> Result<VegeItemData>
    ) {
        viewModelScope.launch {
            if (isInitialLoad) {
                loadState.update { PagingListState.Loading }
            } else {
                loadState.update { PagingListState.Paginating }
            }
            load()
                .onSuccess { vegeItemData ->
                    val updateList: MutableList<VegeItem> = mutableListOf()
                    vegetables.value.map { updateList.add(it) }
                    vegeItemData.datas.map { updateList.add(it) }

                    vegetables.update {
                        updateList
                    }
                    page = vegeItemData.page
                    loadState.update { PagingListState.Success }
                }
                .onFailure {
                    loadState.update {
                        if (isInitialLoad) {
                            PagingListState.Error
                        } else {
                            PagingListState.PaginateError
                        }
                    }
                }
        }
    }
}

enum class PagingListState {
    Initial,
    Loading,
    Error,
    Success,
    Paginating,
    PaginateError,
    End,
}
