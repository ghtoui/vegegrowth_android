package com.moritoui.vegegrowthapp.model

import android.content.Context
import android.util.Log
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException
import kotlinx.serialization.decodeFromString
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
            println("error")
            Log.d("Error", "File Read Error$jsonFilePath")
        }
        return json
    }

    open fun parseFromJson(json: String?): MutableList<VegetableRepository> {
        return when (json) {
            null -> mutableListOf()
            else -> Json.decodeFromString(json)
        }
    }
}
