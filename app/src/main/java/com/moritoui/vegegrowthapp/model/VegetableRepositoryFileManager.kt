package com.moritoui.vegegrowthapp.model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.util.Log
import com.moritoui.vegegrowthapp.usecases.GetSelectVegeItemUseCase
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.FileWriter
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class VegetableRepositoryFileManager @Inject constructor(
    @ApplicationContext applicationContext: Context,
    getSelectVegeItemUseCase: GetSelectVegeItemUseCase
) : FileManagerImpl(applicationContext) {
    private val vegeRepositoryList: List<VegeItemDetail>
    private val imageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
    private val selectVegeItem: VegeItem = getSelectVegeItemUseCase()

    init {
        this.vegeRepositoryList = readVegeRepositoryList(readJsonData(selectVegeItem.uuid))
        readJsonData(fileName = "vegeItemList")
    }

    fun saveVegeRepositoryAndImage(
        vegeRepositoryList: List<VegeItemDetail>,
        takePicImage: Bitmap?
    ) {
        saveImage(
            takePicImage = takePicImage,
            fileName = vegeRepositoryList.last().uuid
        )
        saveVegeRepository(vegeRepositoryList = vegeRepositoryList)
    }

    private fun saveImage(
        takePicImage: Bitmap?,
        fileName: String
    ) {
        val imageFileName = "$fileName.jpg"
        val imageFilePath = File(imageDirectory, imageFileName)
        val outputStream: OutputStream = FileOutputStream(imageFilePath)
        takePicImage?.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)

        saveVegeRepository(vegeRepositoryList = vegeRepositoryList)
    }

    fun saveVegeRepository(vegeRepositoryList: List<VegeItemDetail>) {
        val jsonFileName = "${selectVegeItem.uuid}.json"
        val jsonFilePath = File(applicationContext.filesDir, jsonFileName)
        FileWriter(jsonFilePath).use { stream ->
            stream.write(parseToJson(targetData = vegeRepositoryList))
        }
    }

    fun getImagePath(fileName: String): String {
        val imageFileName = "$fileName.jpg"
        return File(imageDirectory, imageFileName).toString()
    }

    private fun readImage(fileName: String): Bitmap? {
        var takePicImage: Bitmap?
        val imageFileName = "$fileName.jpg"
        val imageFilePath = File(imageDirectory, imageFileName)

        try {
            val inputStream: InputStream = FileInputStream(imageFilePath)
            takePicImage = BitmapFactory.decodeStream(inputStream)
        } catch (e: IOException) {
            takePicImage = null
            Log.d("Error", "File Read Error$imageFileName")
        }
        return takePicImage
    }

    fun getImageList(): List<Bitmap?> {
        var takePicImageList: MutableList<Bitmap?> = mutableListOf()
        vegeRepositoryList.forEach { item ->
            takePicImageList.add(readImage(fileName = item.uuid))
        }
        return takePicImageList
    }

    fun readVegeRepositoryList(json: String?): MutableList<VegeItemDetail> {
        return when (val vegeRepositoryList = parseFromJson<List<VegeItemDetail>>(json)) {
            null -> mutableListOf()
            else -> vegeRepositoryList.toMutableList()
        }
    }

    fun getVegeItem(): VegeItem {
        return selectVegeItem
    }

    fun getVegeRepositoryList(): MutableList<VegeItemDetail> {
        return vegeRepositoryList.toMutableList()
    }
}
