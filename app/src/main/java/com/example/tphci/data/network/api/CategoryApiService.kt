package com.example.tphci.data.network.api

import com.example.tphci.data.network.model.NetworkCategory
import com.example.tphci.data.network.model.NetworkNewCategory
import com.example.tphci.data.network.model.NetworkPagedData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CategoryApiService {
    @POST("categories")
    suspend fun createCategory(@Body categoryData: NetworkNewCategory): Response<NetworkCategory>

    @GET("categories")
    suspend fun getCategories(): Response<NetworkPagedData<NetworkCategory>>

    @PUT("categories/{id}")
    suspend fun updateCategory(
        @Path("id") id: Int,
        @Body categoryData: NetworkNewCategory
    ): Response<NetworkCategory>

    @DELETE("categories/{id}")
    suspend fun deleteCategory(@Path("id") id: Int): Response<Unit>
}