package com.moritoui.vegegrowthapp.model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.util.Log
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class FileManager(
    index: Int,
    private val applicationContext: Context
) {
    private val vegeItem: VegeItem
    private val vegeRepositoryList: List<VegetableRepository>
    private val imageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)

    init {
        this.vegeItem = VegeItemList.getVegeList()[index]
        this.vegeRepositoryList = parseFromJson(readJsonData())
        readJsonData(fileName = "vegeItemList")
    }
    fun readJsonData(fileName: String = vegeItem.uuid): String? {
        var json: String? = null
        val jsonFileName = "$fileName.json"
        val jsonFilePath = File(applicationContext.filesDir, jsonFileName)
        try {
            BufferedReader(FileReader(jsonFilePath)).use { br ->
                json = br.readLine()
            }
        } catch (e: IOException) {
            println("error")
            Log.d("Error", "File Read Error$jsonFilePath")
        }
        return json
    }

    fun saveData(
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
            stream.write(parseToJson(vegeRepositoryList = vegeRepositoryList))
        }
    }

    private fun parseToJson(vegeRepositoryList: List<VegetableRepository>): String {
        return Json.encodeToString(vegeRepositoryList)
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

    fun parseFromJson(json: String?): MutableList<VegetableRepository> {
        return VegetableRepositoryList.getVegeRepositoryList().toMutableList()
        return when (json) {
            null -> mutableListOf()
            else -> Json.decodeFromString(json)
        }
    }

    fun getVegeItem(): VegeItem {
        return vegeItem
    }
    fun getVegeRepositoryList(): MutableList<VegetableRepository> {
        return vegeRepositoryList.toMutableList()
    }
}
