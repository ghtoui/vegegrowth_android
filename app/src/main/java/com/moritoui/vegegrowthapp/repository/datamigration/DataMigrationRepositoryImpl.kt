package com.moritoui.vegegrowthapp.repository.datamigration

import android.content.Context
import androidx.datastore.core.DataStore
import com.moritoui.vegegrowthapp.data.datastores.migrate.MigratePreferences
import com.moritoui.vegegrowthapp.data.room.dao.VegetableDao
import com.moritoui.vegegrowthapp.data.room.dao.VegetableDetailDao
import com.moritoui.vegegrowthapp.data.room.model.VegetableDetailEntity
import com.moritoui.vegegrowthapp.data.room.model.toVegeItem
import com.moritoui.vegegrowthapp.model.FileManagerImpl
import com.moritoui.vegegrowthapp.model.VegeItemDetail
import com.moritoui.vegegrowthapp.model.VegetableRepositoryFileManager
import com.moritoui.vegegrowthapp.model.toVegeTableEntity
import com.moritoui.vegegrowthapp.repository.VegeItemDetailRepositoryImpl
import com.moritoui.vegegrowthapp.repository.VegeItemListRepositoryImpl
import com.moritoui.vegegrowthapp.usecases.GetOldTakePictureFilePathListUseCase
import com.moritoui.vegegrowthapp.usecases.GetOldVegeItemDetailListUseCase
import com.moritoui.vegegrowthapp.usecases.GetSelectVegeItemUseCase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class DataMigrationRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val vegetableDao: VegetableDao,
    private val vegetableDetailDao: VegetableDetailDao,
    private val migratePreferences: DataStore<MigratePreferences>
) : DataMigrationRepository {
    override var isDataMigrating: MutableStateFlow<Boolean> = MutableStateFlow(false)

    private var vegeItemDetailList: MutableList<VegeItemDetail> = mutableListOf()
    private var picturePathList: List<String> = emptyList()

    /**
     * Roomに保存されていないデータを保存する
     */
    override suspend fun dataMigration() {
        val isMigrate: Boolean = migratePreferences.data.first().isMigrated
        val vegeItemListRepository = VegeItemListRepositoryImpl(FileManagerImpl(context), vegetableDao)
        var vegeItemList = vegeItemListRepository.sortItemList()
        if (vegeItemList.isEmpty() || isMigrate) {
            return
        }

        isDataMigrating.update {
            true
        }

        vegeItemListRepository.selectIndex = 0
        vegeItemList.forEach {
            vegetableDao.upsertVegetable(it.toVegeTableEntity())
        }
        vegeItemList = vegetableDao.getVegetables().map { it.toVegeItem() }.toMutableList()
        vegeItemList.forEachIndexed { itemIndex, item ->
            vegeItemListRepository.selectIndex = itemIndex
            val getSelectedIndexUseCase = GetSelectVegeItemUseCase(vegeItemListRepository)
            val vegeItemDetailRepository = VegeItemDetailRepositoryImpl(
                VegetableRepositoryFileManager(context, getSelectedIndexUseCase)
            )
            val getVegeItemDetailListUseCase = GetOldVegeItemDetailListUseCase(vegeItemDetailRepository)
            val getTakePictureFilePathListUseCase = GetOldTakePictureFilePathListUseCase(
                vegeItemDetailRepository
            )
            vegeItemDetailList = getVegeItemDetailListUseCase()
            picturePathList = getTakePictureFilePathListUseCase()
            for (i in picturePathList.indices) {
                vegetableDetailDao.upsertVegetableDetail(
                    VegetableDetailEntity(
                        vegetableId = item.id,
                        imagePath = picturePathList[i],
                        name = item.name,
                        date = vegeItemDetailList[i].date,
                        note = vegeItemDetailList[i].memo,
                        size = vegeItemDetailList[i].size,
                        uuid = vegeItemDetailList[i].uuid,
                    )
                )
            }
        }
        isDataMigrating.update {
            false
        }

        migratePreferences.updateData {
            it.copy(
                isMigrated = true
            )
        }
    }
}
