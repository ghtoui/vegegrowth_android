package com.moritoui.vegegrowthapp.repository

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
    fun changeVegeItemStatus(vegeItem: VegeItem)
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
        fileManager.saveVegeItemListData(vegeItemList)
    }

    override fun setSelectedSortStatus(sortStatus: SortStatus) {
        this.sortStatus = sortStatus
        vegeItemList = sortItemList()
    }

    /**
     * ソートした状態のリストで削除・追加・変更をしてから、保存をすると
     * ソート外の表示されていないものは保存されずに失われてしまう
     */
    override fun deleteVegeItem(deleteItemList: MutableList<VegeItem>) {
        // 要素を削除するときは、並び替えているときでもリスト全体から削除する必要がある
        vegeItemList = loadVegeItemList()
        deleteItemList.forEach { item ->
            vegeItemList.remove(item)
        }
        saveVegeItemList()
    }

    override fun addVegeItem(vegeItem: VegeItem) {
        // 要素を変更するときは、並び替えているときでもリスト全体で変える必要がある
        vegeItemList = loadVegeItemList()
        vegeItemList.add(vegeItem)
        saveVegeItemList()
    }

    override fun changeVegeItemStatus(vegeItem: VegeItem) {
        // 要素を変更するときは、並び替えているときでもリスト全体で変える必要がある
        vegeItemList = loadVegeItemList().map {
            if (it.uuid == vegeItem.uuid) {
                it.status = vegeItem.status
            }
            it
        }.toMutableList()
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

    // 保存されている全てのデータを持ってくる
    private fun loadVegeItemList(): MutableList<VegeItem> {
        return fileManager.getVegeItemList()
    }
}
