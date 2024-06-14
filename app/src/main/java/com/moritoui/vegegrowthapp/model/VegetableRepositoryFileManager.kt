package com.moritoui.vegegrowthapp.model

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import android.util.Log
import androidx.core.content.ContextCompat
import com.moritoui.vegegrowthapp.usecases.GetOldSelectVegeItemUseCase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter
import java.io.IOException
import java.io.OutputStream
import java.nio.file.Files
import javax.inject.Inject

class VegetableRepositoryFileManager
    @Inject
    constructor(
        @ApplicationContext applicationContext: Context,
        getSelectVegeItemUseCase: GetOldSelectVegeItemUseCase,
    ) : FileManagerImpl(applicationContext) {
        private var vegeRepositoryList: List<VegeItemDetail>
        private val imageDirectory = ContextCompat.getDataDir(applicationContext)
        private val oldImageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        private val selectVegeItem: VegeItem = getSelectVegeItemUseCase()

        init {
            this.vegeRepositoryList = readVegeRepositoryList(readJsonData(selectVegeItem.uuid))
            // 非同期処理で昔のファイルパスで保存していたものを新しいパスに移動する
            val scope = CoroutineScope(Dispatchers.IO)
            scope.launch {
                oldImagePath()
            }
        }

        fun saveVegeRepositoryAndImage(
            vegeRepositoryList: List<VegeItemDetail>,
            takePicImage: Bitmap?,
        ) {
            saveImage(
                takePicImage = takePicImage,
                fileName = vegeRepositoryList.last().uuid,
            )
            saveVegeRepository(vegeRepositoryList = vegeRepositoryList)
            this.vegeRepositoryList = vegeRepositoryList
        }

        private fun saveImage(
            takePicImage: Bitmap?,
            fileName: String,
        ) {
            val imageFileName = "$fileName.jpg"
            val imageFilePath = File(imageDirectory, imageFileName)
            val outputStream: OutputStream = FileOutputStream(imageFilePath)
            takePicImage?.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)
        }

        fun saveVegeRepository(vegeRepositoryList: List<VegeItemDetail>) {
            val jsonFileName = "${selectVegeItem.uuid}.json"
            val jsonFilePath = File(applicationContext.filesDir, jsonFileName)
            FileWriter(jsonFilePath).use { stream ->
                stream.write(parseToJson(targetData = vegeRepositoryList))
            }
        }

        private fun getImagePath(fileName: String): String {
            val imageFileName = "$fileName.jpg"
            return File(imageDirectory, imageFileName).toString()
        }

        fun getImagePathList(): List<String> =
            vegeRepositoryList.map {
                getImagePath(it.uuid)
            }

        // 非同期処理で実行
        // 古いパスで保存していた写真を新しいパスに移動させる
        private suspend fun oldImagePath() {
            withContext(Dispatchers.IO) {
                vegeRepositoryList.forEach { item ->
                    val oldPath = File(oldImageDirectory, "${item.uuid}.jpg")
                    val newPath = File(imageDirectory, "${item.uuid}.jpg")
                    try {
                        Files.move(oldPath.toPath(), newPath.toPath())
                        Log.d("IO", "Success")
                    } catch (e: IOException) {
                        Log.d("IO", e.toString())
                    }
                }
            }
        }

        private fun readVegeRepositoryList(json: String?): MutableList<VegeItemDetail> =
            when (val vegeRepositoryList = parseFromJson<List<VegeItemDetail>>(json)) {
                null -> mutableListOf()
                else -> vegeRepositoryList.toMutableList()
            }

        fun getVegeRepositoryList(): MutableList<VegeItemDetail> = vegeRepositoryList.toMutableList()
    }
