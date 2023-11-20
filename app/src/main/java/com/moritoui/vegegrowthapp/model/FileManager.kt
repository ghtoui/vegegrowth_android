package com.moritoui.vegegrowthapp.model

import android.content.Context
import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Singleton

interface FileManagerImpl {
    val applicationContext: Context
    fun readJsonData(fileName: String): String?
    fun saveVegeItemListData(vegeItemList: List<VegeItem>)
    fun getVegeItemList(): MutableList<VegeItem>
}

@Module
@InstallIn(SingletonComponent::class)
object FileMangerModule {
    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }
    @Provides
    @Singleton
    fun provideFileManager(context: Context): FileManagerImpl {
        return FileManager(context)
    }
}

open class FileManager (
    override val applicationContext: Context
) : FileManagerImpl {

    init {
        Log.d("auto", "ok")
    }

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

    override fun getVegeItemList(): MutableList<VegeItem> {
        return when (val vegeItemList = parseFromJson<List<VegeItem>>(readJsonData(fileName = "vegeItemList"))) {
            null -> mutableListOf()
            else -> vegeItemList.toMutableList()
        }
    }

    private inline fun <reified T> parseFromJson(json: String?): T? {
        return when (json) {
            null -> null
            else -> Json.decodeFromString<T>(json)
        }
    }

    private inline fun <reified T> parseToJson(targetData: T): String {
        return Json.encodeToString(targetData)
    }
}
