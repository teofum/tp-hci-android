package com.example.tphci.data.network.api

import com.example.tphci.data.network.model.NetworkNewShoppingList
import com.example.tphci.data.network.model.NetworkPagedShopingLists
import com.example.tphci.data.network.model.NetworkPurchaseShoppingList
import com.example.tphci.data.network.model.NetworkShoppingList
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ShoppingListApiService {
    @POST("shopping-lists")
    suspend fun createList(@Body listData: NetworkNewShoppingList): Response<NetworkShoppingList>

    @GET("shopping-lists")
    suspend fun getLists(): Response<NetworkPagedShopingLists>

    @PUT("shopping-lists/{id}")
    suspend fun updateList(
        @Path("id") id: Int,
        @Body productData: NetworkNewShoppingList
    ): Response<NetworkShoppingList>

    @DELETE("shopping-lists/{id}")
    suspend fun deleteList(@Path("id") id: Int): Response<Unit>

    @POST("shopping-lists/{id}/purchase")
    suspend fun purchaseList(
        @Path("id") id: Int,
        @Body data: NetworkPurchaseShoppingList
    ): Response<NetworkShoppingList>

    // TODO share
}