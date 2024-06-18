package com.moritoui.vegegrowthapp.repository

import com.moritoui.vegegrowthapp.data.room.dao.VegetableDao
import com.moritoui.vegegrowthapp.data.room.model.toVegeItem
import com.moritoui.vegegrowthapp.model.FileManager
import com.moritoui.vegegrowthapp.model.FilterStatus
import com.moritoui.vegegrowthapp.model.VegeItem
import com.moritoui.vegegrowthapp.model.filterStatusMap
import com.moritoui.vegegrowthapp.model.toVegeTableEntity
import javax.inject.Inject

interface VegeItemListRepository {
    var vegeItemList: MutableList<VegeItem>
    var selectIndex: Int?
    var filterStatus: FilterStatus

    fun setSelectedIndex(index: Int)

    fun setSelectedSortStatus(filterStatus: FilterStatus)

    fun deleteVegeItem(deleteItemList: MutableList<VegeItem>)

    fun addVegeItem(vegeItem: VegeItem)

    fun sortItemList(): MutableList<VegeItem>

    fun saveVegeItemList()

    fun changeVegeItemStatus(vegeItem: VegeItem)

    suspend fun insertVegetable(vegeItem: VegeItem)

    suspend fun getVegetables(): List<VegeItem>
}

class VegeItemListRepositoryImpl
@Inject
constructor(private val fileManager: FileManager, private val vegetableDao: VegetableDao) : VegeItemListRepository {
    override var vegeItemList: MutableList<VegeItem> = loadVegeItemList()

    // 画面遷移前はnullだが、画面遷移時にはnullではなくなるはず
    override var selectIndex: Int? = null
    override var filterStatus: FilterStatus = FilterStatus.All

    override fun setSelectedIndex(index: Int) {
        selectIndex = index
    }

    override fun saveVegeItemList() {
        fileManager.saveVegeItemListData(vegeItemList)
    }

    override fun setSelectedSortStatus(filterStatus: FilterStatus) {
        this.filterStatus = filterStatus
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
        vegeItemList =
            loadVegeItemList()
                .map {
                    if (it.uuid == vegeItem.uuid) {
                        it.status = vegeItem.status
                    }
                    it
                }.toMutableList()
    }

    override fun sortItemList(): MutableList<VegeItem> {
        val loadVegeItemList = loadVegeItemList()
        return when (filterStatus) {
            FilterStatus.All -> loadVegeItemList
            else -> {
                loadVegeItemList
                    .filter { item ->
                        item.status == filterStatusMap[filterStatus] ||
                            item.category == filterStatusMap[filterStatus]
                    }.toMutableList()
            }
        }
    }

    override suspend fun insertVegetable(vegeItem: VegeItem) {
        vegetableDao.upsertVegetable(vegeItem.toVegeTableEntity())
    }

    override suspend fun getVegetables(): List<VegeItem> = vegetableDao.getVegetables().map {
        it.toVegeItem()
    }

    // 保存されている全てのデータを持ってくる
    private fun loadVegeItemList(): MutableList<VegeItem> = fileManager.getVegeItemList()
}
