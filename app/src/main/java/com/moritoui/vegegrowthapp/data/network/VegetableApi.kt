package com.moritoui.vegegrowthapp.data.network

import com.moritoui.vegegrowthapp.data.network.model.VegeItem
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET

private const val BASE_URL = "http://localhost:8000"
private val retrofit = Retrofit.Builder()

interface VegetableApi {
    @GET("/api/vegetables")
    suspend fun getVegetables(): Response<List<VegeItem>>
}
