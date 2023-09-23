package com.moritoui.vegegrowthapp.model

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import android.util.Log
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException
import java.io.OutputStream
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class FileManager(
    index: Int,
    private val applicationContext: Context
) {
    private val vegeItem: VegeItem

    init {
        this.vegeItem = VegeItemList.getVegeList()[index]
    }
    fun readJsonData(): String? {
        var json: String? = null
        val jsonFileName = "${vegeItem.uuid}.json"
        val jsonFilePath = File(applicationContext.filesDir, jsonFileName)
        try {
            BufferedReader(FileReader(jsonFilePath)).use { br ->
                json = br.readLine()
            }
        } catch (e: IOException) {
            Log.d("Error", "File Read Error$jsonFilePath")
        }
        return json
    }

    fun saveData(
        vegeList: List<VegetableRepository>,
        takePicImage: Bitmap?
    ) {
        val imageFileName = "${vegeItem.uuid}.jpg"
        val imageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val imageFilePath = File(imageDirectory, imageFileName)
        val outputStream: OutputStream = FileOutputStream(imageFilePath)
        takePicImage?.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)

        val jsonFileName = "${vegeItem.uuid}.json"
        val jsonFilePath = File(applicationContext.filesDir, jsonFileName)
        FileWriter(jsonFilePath).use { stream ->
            stream.write(parseToJson(vegeList = vegeList))
        }
    }

    private fun parseToJson(vegeList: List<VegetableRepository>): String {
        return Json.encodeToString(vegeList)
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
}
