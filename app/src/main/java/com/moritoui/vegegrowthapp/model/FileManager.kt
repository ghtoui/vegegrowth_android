package com.moritoui.vegegrowthapp.model

import android.content.Context
import android.util.Log
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

open class FileManager(
    private val applicationContext: Context
) {

    fun readJsonData(fileName: String): String? {
        var json: String? = null
        val jsonFileName = "$fileName.json"
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

    inline fun <reified T> parseFromJson(json: String?): T? {
        return when (json) {
            null -> null
            else -> Json.decodeFromString<T>(json)
        }
    }

    fun saveVegeItemListData(vegeItemList: List<VegeItem>) {
        val jsonFileName = "vegeItemList.json"
        val jsonFilePath = File(applicationContext.filesDir, jsonFileName)
        FileWriter(jsonFilePath).use { stream ->
            stream.write(parseToJson(targetData = vegeItemList))
        }
    }

    inline fun <reified T> parseToJson(targetData: T): String {
        return Json.encodeToString(targetData)
    }

    fun getVegeItemList(): MutableList<VegeItem> {
        return when (val vegeItemList = parseFromJson<List<VegeItem>>(readJsonData(fileName = "vegeItemList"))) {
            null -> mutableListOf()
            else -> vegeItemList.toMutableList()
        }
    }
}
