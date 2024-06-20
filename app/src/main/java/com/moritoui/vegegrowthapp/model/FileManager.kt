package com.moritoui.vegegrowthapp.model

import android.content.Context
import android.util.Log
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException

interface FileManager {
    val applicationContext: Context

    fun readJsonData(fileName: String): String?

    fun saveVegeItemListData(vegeItemList: List<VegeItem>)

    fun getVegeItemList(): MutableList<VegeItem>
}

open class FileManagerImpl(override val applicationContext: Context) : FileManager {
    override fun readJsonData(fileName: String): String? {
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

    override fun saveVegeItemListData(vegeItemList: List<VegeItem>) {
        val jsonFileName = "vegeItemList.json"
        val jsonFilePath = File(applicationContext.filesDir, jsonFileName)
        FileWriter(jsonFilePath).use { stream ->
            stream.write(parseToJson(targetData = vegeItemList))
        }
    }

    override fun getVegeItemList(): MutableList<VegeItem> = when (
        val vegeItemList = parseFromJson<List<VegeItem>>(
            readJsonData(fileName = "vegeItemList")
        )
    ) {
        null -> mutableListOf()
        else -> vegeItemList.toMutableList()
    }

    inline fun <reified T> parseFromJson(json: String?): T? = when (json) {
        null -> null
        else -> Json.decodeFromString<T>(json)
    }

    inline fun <reified T> parseToJson(targetData: T): String = Json.encodeToString(targetData)
}
