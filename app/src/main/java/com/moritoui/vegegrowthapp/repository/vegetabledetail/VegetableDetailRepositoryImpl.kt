package com.moritoui.vegegrowthapp.repository.vegetabledetail

import android.content.Context
import android.graphics.Bitmap
import androidx.core.content.ContextCompat
import com.moritoui.vegegrowthapp.data.room.dao.VegetableDetailDao
import com.moritoui.vegegrowthapp.model.VegeItemDetail
import com.moritoui.vegegrowthapp.model.toVegetableEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.UUID
import javax.inject.Inject

class VegetableDetailRepositoryImpl
    @Inject
    constructor(
        private val vegetableDetailDao: VegetableDetailDao,
        @ApplicationContext context: Context,
    ) : VegetableDetailRepository {
        private val imageDirectory = ContextCompat.getDataDir(context)

        override suspend fun addVegeItemDetail(vegeItemDetail: VegeItemDetail) {
            vegetableDetailDao.upsertVegetableDetail(vegeItemDetail.toVegetableEntity())
        }

        override suspend fun editMemo(
            memo: String,
            vegeItemDetail: VegeItemDetail,
        ) {
            vegeItemDetail.memo = memo
            vegetableDetailDao.upsertVegetableDetail(vegeItemDetail.toVegetableEntity())
        }

        override fun saveTookPicture(tookPicture: Bitmap): String {
            val fileName = UUID.randomUUID().toString()
            val imageFileName = "$fileName.jpg"
            val imageFilePath = File(imageDirectory, imageFileName)
            val outputStream: OutputStream = FileOutputStream(imageFilePath)
            tookPicture.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)
            return imageFilePath.path
        }
    }
