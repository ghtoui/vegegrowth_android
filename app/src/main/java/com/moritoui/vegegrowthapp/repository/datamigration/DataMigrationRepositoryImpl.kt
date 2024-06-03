package com.moritoui.vegegrowthapp.repository.datamigration

import android.content.Context
import com.moritoui.vegegrowthapp.data.room.dao.VegetableDao
import com.moritoui.vegegrowthapp.data.room.dao.VegetableDetailDao
import com.moritoui.vegegrowthapp.data.room.model.VegetableDetailEntity
import com.moritoui.vegegrowthapp.data.room.model.toVegeItem
import com.moritoui.vegegrowthapp.model.FileManagerImpl
import com.moritoui.vegegrowthapp.model.VegeItem
import com.moritoui.vegegrowthapp.model.VegeItemDetail
import com.moritoui.vegegrowthapp.model.VegetableRepositoryFileManager
import com.moritoui.vegegrowthapp.model.toVegeTableEntity
import com.moritoui.vegegrowthapp.repository.VegeItemDetailRepositoryImpl
import com.moritoui.vegegrowthapp.repository.VegeItemListRepositoryImpl
import com.moritoui.vegegrowthapp.usecases.GetSelectVegeItemUseCase
import com.moritoui.vegegrowthapp.usecases.GetTakePictureFilePathListUseCase
import com.moritoui.vegegrowthapp.usecases.GetVegeItemDetailListUseCase
import com.moritoui.vegegrowthapp.usecases.GetVegeItemListUseCase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class DataMigrationRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val vegetableDao: VegetableDao,
    private val vegetableDetailDao: VegetableDetailDao,
    getVegeItemListUseCase: GetVegeItemListUseCase,
) : DataMigrationRepository {
    override var isDataMigrating: MutableStateFlow<Boolean> = MutableStateFlow(false)

    private var vegeItemList: MutableList<VegeItem> = getVegeItemListUseCase()
    private var vegeItemDetailList: MutableList<VegeItemDetail> = mutableListOf()
    private var picturePathList: List<String> = emptyList()

    /**
     * Roomに保存されていないデータを保存する
     */
    override suspend fun dataMigration() {
        if (vegeItemList.isEmpty()) {
            return
        }
        isDataMigrating.update {
            true
        }
        val vegeItemListRepository = VegeItemListRepositoryImpl(FileManagerImpl(context), vegetableDao)
        vegeItemListRepository.selectIndex = 0
        val getSelectedIndexUseCase = GetSelectVegeItemUseCase(vegeItemListRepository)
        val vegeItemDetailRepository = VegeItemDetailRepositoryImpl(
            VegetableRepositoryFileManager(context, getSelectedIndexUseCase)
        )
        val getVegeItemDetailListUseCase = GetVegeItemDetailListUseCase(vegeItemDetailRepository)
        val getTakePictureFilePathListUseCase = GetTakePictureFilePathListUseCase(
            vegeItemDetailRepository
        )
        vegeItemList.forEach {
            vegetableDao.insertVegetable(it.toVegeTableEntity())
        }
        vegeItemList = vegetableDao.getVegetables().map { it.toVegeItem() }.toMutableList()
        vegeItemList.forEach {
            vegeItemDetailList = getVegeItemDetailListUseCase()
            picturePathList = getTakePictureFilePathListUseCase()
            for (i in 0..picturePathList.size) {
                vegetableDetailDao.insertVegetableDetail(
                    VegetableDetailEntity(
                        vegetableId = it.id,
                        imagePath = picturePathList[i],
                        name = it.name,
                        date = vegeItemDetailList[i].date,
                        note = vegeItemDetailList[i].memo,
                        size = vegeItemDetailList[i].size,
                        uuid = vegeItemDetailList[i].uuid,
                    )
                )
            }
        }
        isDataMigrating.update {
            true
        }
    }
}
