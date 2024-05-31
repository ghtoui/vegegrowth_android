package com.moritoui.vegegrowthapp.repository.datamigration

import android.content.Context
import android.util.Log
import com.moritoui.vegegrowthapp.data.room.dao.VegetableDao
import com.moritoui.vegegrowthapp.data.room.dao.VegetableDetailDao
import com.moritoui.vegegrowthapp.model.FileManagerImpl
import com.moritoui.vegegrowthapp.model.VegeItem
import com.moritoui.vegegrowthapp.model.VegeItemDetail
import com.moritoui.vegegrowthapp.model.VegetableRepositoryFileManager
import com.moritoui.vegegrowthapp.repository.VegeItemDetailRepositoryImpl
import com.moritoui.vegegrowthapp.repository.VegeItemListRepositoryImpl
import com.moritoui.vegegrowthapp.usecases.GetSelectVegeItemUseCase
import com.moritoui.vegegrowthapp.usecases.GetTakePictureFilePathListUseCase
import com.moritoui.vegegrowthapp.usecases.GetVegeItemDetailListUseCase
import com.moritoui.vegegrowthapp.usecases.GetVegeItemListUseCase
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DataMigrationRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val vegetableDao: VegetableDao,
    private val vegetableDetailDao: VegetableDetailDao,
    getVegeItemListUseCase: GetVegeItemListUseCase,
) : DataMigrationRepository {

    private var vegeItemList: MutableList<VegeItem> = getVegeItemListUseCase()
    private var vegeItemDetailList: MutableList<VegeItemDetail> = mutableListOf()
    private var picturePathList: List<String> = emptyList()

    override suspend fun dataMigration() {
        if (vegeItemList.isEmpty()) {
            return
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
            vegeItemDetailList = getVegeItemDetailListUseCase()
            picturePathList = getTakePictureFilePathListUseCase()
            Log.d("test", "${vegeItemList} \n" +
                    "${vegeItemDetailList}\n" +
                    "${picturePathList}")
        }
    }
}
