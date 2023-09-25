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
    private val applicationContext: Context
) : FileManager(applicationContext = applicationContext) {
    private val vegeItem: VegeItem
    private val vegeRepositoryList: List<VegetableRepository>
    private val imageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)

    init {
        this.vegeItem = getVegeItemList()[index]
        this.vegeRepositoryList = readVegeRepositoryList(readJsonData(vegeItem.uuid))
        readJsonData(fileName = "vegeItemList")
    }

    fun saveVegeRepositoryData(
        vegeRepositoryList: List<VegetableRepository>,
        takePicImage: Bitmap?
    ) {
        val imageFileName = "${vegeItem.uuid}.jpg"
        val imageFilePath = File(imageDirectory, imageFileName)
        val outputStream: OutputStream = FileOutputStream(imageFilePath)
        takePicImage?.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)

        val jsonFileName = "${vegeItem.uuid}.json"
        val jsonFilePath = File(applicationContext.filesDir, jsonFileName)
        FileWriter(jsonFilePath).use { stream ->
            stream.write(parseToJson(targetData = vegeRepositoryList))
        }
    }

    fun getImageList(): List<Bitmap?> {
        var takePicImageList: MutableList<Bitmap?> = mutableListOf()
        var takePicImage: Bitmap?
        vegeRepositoryList.forEach { item ->
            val imageFileName = "${item.uuid}.jpg"
            val imageFilePath = File(imageDirectory, imageFileName)
            try {
                val inputStream: InputStream = FileInputStream(imageFilePath)
                takePicImage = BitmapFactory.decodeStream(inputStream)
            } catch (e: IOException) {
                takePicImage = null
                Log.d("Error", "File Read Error$imageFileName")
            }
            takePicImageList.add(takePicImage)
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
