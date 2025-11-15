package com.example.tphci.data.network.api

import com.example.tphci.data.network.model.NetworkNewProduct
import com.example.tphci.data.network.model.NetworkPagedData
import com.example.tphci.data.network.model.NetworkProduct
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProductApiService {
    @POST("products")
    suspend fun createProduct(@Body productData: NetworkNewProduct): Response<NetworkProduct>

    @GET("products")
    suspend fun getProducts(): Response<NetworkPagedData<NetworkProduct>>

    @PUT("products/{id}")
    suspend fun updateProduct(
        @Path("id") id: Int,
        @Body productData: NetworkNewProduct
    ): Response<NetworkProduct>

    @DELETE("products/{id}")
    suspend fun deleteProduct(@Path("id") id: Int): Response<Unit>
}