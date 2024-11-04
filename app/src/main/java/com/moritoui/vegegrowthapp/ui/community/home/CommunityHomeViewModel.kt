package com.moritoui.vegegrowthapp.ui.community.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moritoui.vegegrowthapp.data.network.VegetableApi
import com.moritoui.vegegrowthapp.model.VegeItem
import com.moritoui.vegegrowthapp.model.VegeItemData
import com.moritoui.vegegrowthapp.ui.community.home.model.CommunityHomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommunityHomeViewModel @Inject constructor(
    private val vegetableApi: VegetableApi,
) : ViewModel() {
    private val vegeItemData: MutableStateFlow<VegeItemData?> = MutableStateFlow<VegeItemData?>(null).apply {
        viewModelScope.launch {
            value = vegetableApi.getVegetables(page = 0).body()?.toDomain()
        }
    }

    private val isAutoAppendLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)

    val uiState: StateFlow<CommunityHomeState> = combine(
        vegeItemData,
        isAutoAppendLoading,
    ) { vegeItemData, isAutoAppendLoading ->
        CommunityHomeState(
            datas = vegeItemData?.datas ?: emptyList(),
            isAutoAppendLoading = isAutoAppendLoading,
        )
    }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = CommunityHomeState.initial()
        )

    fun getList() {
        viewModelScope.launch {
            val data = vegetableApi.getVegetables(page = 0)
            vegeItemData.update {
                data.body()?.toDomain()
            }
        }
    }

    fun autoAppend() {
        viewModelScope.launch {
            isAutoAppendLoading.value = true
            // TODO: ダミーのローディング
            delay(1000L)

            val data = vegetableApi.getVegetables(page = vegeItemData.value?.page?.plus(1)  ?: 0)
            val updateList: MutableList<VegeItem> = mutableListOf()
            vegeItemData.value?.datas?.map {
                updateList.add(it)
            }
            data.body()?.datas?.map {
                updateList.add(it.toDomain())
            }
            vegeItemData.update {
                VegeItemData(
                    datas = updateList.toList(),
                    page = data.body()?.page ?: 0
                )
            }
            isAutoAppendLoading.value = false
        }
    }
}
