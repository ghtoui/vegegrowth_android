package com.moritoui.vegegrowthapp.ui.timeline.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.moritoui.vegegrowthapp.data.network.datasource.TimelinePagingDataSource
import com.moritoui.vegegrowthapp.model.VegeItem
import com.moritoui.vegegrowthapp.ui.timeline.home.model.TimelineHomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimelineHomeViewModel @Inject constructor(
    private val timelinePagingDataSource: TimelinePagingDataSource
) : ViewModel() {
    private val vegetables: MutableStateFlow<PagingData<VegeItem>> = MutableStateFlow<PagingData<VegeItem>>(
        PagingData.empty()
    ).apply {
        viewModelScope.launch {
            Pager(
                config = PagingConfig(
                        10, enablePlaceholders = true
                        )
            ) {
                timelinePagingDataSource
            }.flow.cachedIn(viewModelScope).collect {
                value = it
            }
        }
    }

    val uiState: StateFlow<TimelineHomeState> = vegetables.map {
        TimelineHomeState(
            vegetables = it
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TimelineHomeState.initial()
    )
}
