package com.moritoui.vegegrowthapp.data.network

import com.moritoui.vegegrowthapp.data.network.model.VegeItemData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface VegetableApi {
    @GET("/api/vegetables")
    suspend fun getVegetables(
        @Query("page") page: Int,
    ): Response<VegeItemData>
}
