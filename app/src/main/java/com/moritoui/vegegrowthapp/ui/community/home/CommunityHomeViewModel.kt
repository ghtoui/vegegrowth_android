package com.moritoui.vegegrowthapp.ui.community.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moritoui.vegegrowthapp.data.network.VegetableApi
import com.moritoui.vegegrowthapp.model.VegeItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommunityHomeViewModel @Inject constructor(
    private val vegetableApi: VegetableApi,
) : ViewModel() {
    private val list: MutableStateFlow<List<VegeItem>> = MutableStateFlow<List<VegeItem>>(emptyList()).apply {
        viewModelScope.launch {
            value = vegetableApi.getVegetables(1).body()?.datas?.map {
                it.toDomain()
            } ?: emptyList()
        }
    }

    val listState: StateFlow<List<VegeItem>> = list.map {
        it
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList(),
    )

    init {
        viewModelScope.launch {
            list.collect {
                Log.d("test", it.toString())
            }
        }
    }

    fun getList() {
        viewModelScope.launch {
            val data = vegetableApi.getVegetables(page = 1)
            Log.d("test", "$data")
            Log.d("test", "${data.body()}")
            list.update {
                data?.body()?.datas?.map {
                    it.toDomain()
                } ?: emptyList()
            }
        }
    }
}
