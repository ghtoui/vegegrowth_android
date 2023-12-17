package com.moritoui.vegegrowthapp.repository

import android.util.Log
import com.moritoui.vegegrowthapp.model.FileManager
import com.moritoui.vegegrowthapp.model.SortStatus
import com.moritoui.vegegrowthapp.model.VegeItem
import com.moritoui.vegegrowthapp.model.sortStatusMap
import javax.inject.Inject

interface VegeItemListRepository {
    var vegeItemList: MutableList<VegeItem>
    var selectIndex: Int?
    var sortStatus: SortStatus
    fun setSelectedIndex(index: Int)
    fun setSelectedSortStatus(sortStatus: SortStatus)
    fun deleteVegeItem(deleteItemList: MutableList<VegeItem>)
    fun addVegeItem(vegeItem: VegeItem)
    fun sortItemList(): MutableList<VegeItem>
    fun saveVegeItemList()
}
class VegeItemListRepositoryImpl @Inject constructor(
    private val fileManager: FileManager,
) : VegeItemListRepository {
    override var vegeItemList: MutableList<VegeItem> = loadVegeItemList()
    // 画面遷移前はnullだが、画面遷移時にはnullではなくなるはず
    override var selectIndex: Int? = null
    override var sortStatus: SortStatus = SortStatus.All

    override fun setSelectedIndex(index: Int) {
        selectIndex = index
    }

    override fun saveVegeItemList() {
        Log.d("test", vegeItemList.toString())
        fileManager.saveVegeItemListData(vegeItemList)
    }

    override fun setSelectedSortStatus(sortStatus: SortStatus) {
        this.sortStatus = sortStatus
        vegeItemList = sortItemList()
    }

    override fun deleteVegeItem(deleteItemList: MutableList<VegeItem>) {
        deleteItemList.forEach { item ->
            vegeItemList.remove(item)
        }
        saveVegeItemList()
    }

    override fun addVegeItem(vegeItem: VegeItem) {
        vegeItemList.add(vegeItem)
        saveVegeItemList()
    }

    override fun sortItemList(): MutableList<VegeItem> {
        val loadVegeItemList = loadVegeItemList()
        return when (sortStatus) {
            SortStatus.All -> loadVegeItemList
            else -> {
                loadVegeItemList.filter { item ->
                    item.status == sortStatusMap[sortStatus] || item.category == sortStatusMap[sortStatus]
                }.toMutableList()
            }
        }
    }

    private fun loadVegeItemList(): MutableList<VegeItem> {
        return fileManager.getVegeItemList()
    }
}
