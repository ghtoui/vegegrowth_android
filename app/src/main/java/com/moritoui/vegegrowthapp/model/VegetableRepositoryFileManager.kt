package com.moritoui.vegegrowthapp.model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.util.Log
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.FileWriter
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class VegetableRepositoryFileManager(
    index: Int,
    private val sortText: String,
    private val applicationContext: Context
) : FileManager(applicationContext = applicationContext) {
    private val vegeItem: VegeItem
    private val vegeRepositoryList: List<VegetableRepository>
    private val imageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)

    init {
        this.vegeItem = sortItemList(getVegeItemList())[index]
        this.vegeRepositoryList = readVegeRepositoryList(readJsonData(vegeItem.uuid))
        readJsonData(fileName = "vegeItemList")
    }

    private fun sortItemList(vegeItemList: MutableList<VegeItem>): MutableList<VegeItem> {
        return when (val sortStatus = SortStatus.valueOf(sortText)) {
            SortStatus.All -> vegeItemList
            else -> {
                vegeItemList.filter { item ->
                    item.status == sortStatusMap[sortStatus] || item.category == sortStatusMap[sortStatus]
                }.toMutableList()
            }
        }
    }

    fun saveVegeRepositoryAndImage(
        vegeRepositoryList: List<VegetableRepository>,
        takePicImage: Bitmap?
    ) {
        saveImage(
            takePicImage = takePicImage,
            fileName = vegeRepositoryList.last().uuid
        )
        saveVegeRepository(vegeRepositoryList = vegeRepositoryList)
    }

    fun saveImage(
        takePicImage: Bitmap?,
        fileName: String
    ) {
        val imageFileName = "$fileName.jpg"
        val imageFilePath = File(imageDirectory, imageFileName)
        val outputStream: OutputStream = FileOutputStream(imageFilePath)
        takePicImage?.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)

        saveVegeRepository(vegeRepositoryList = vegeRepositoryList)
    }

    fun saveVegeRepository(vegeRepositoryList: List<VegetableRepository>) {
        val jsonFileName = "${vegeItem.uuid}.json"
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

    fun readVegeRepositoryList(json: String?): MutableList<VegetableRepository> {
        return when (val vegeRepositoryList = parseFromJson<List<VegetableRepository>>(json)) {
            null -> mutableListOf()
            else -> vegeRepositoryList.toMutableList()
        }
    }

    fun getVegeItem(): VegeItem {
        return vegeItem
    }

    fun getVegeRepositoryList(): MutableList<VegetableRepository> {
        return vegeRepositoryList.toMutableList()
    }
}
