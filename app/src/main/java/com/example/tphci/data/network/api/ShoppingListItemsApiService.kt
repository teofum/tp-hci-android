package com.example.tphci.data.network.api

import com.example.tphci.data.network.model.NetworkItem
import com.example.tphci.data.network.model.NetworkItemPurchased
import com.example.tphci.data.network.model.NetworkNewItem
import com.example.tphci.data.network.model.NetworkPagedItems
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ShoppingListItemsApiService {

    @GET("shopping-lists/{listId}/items")
    suspend fun getListItems(
        @Path("listId") listId: Int,
        @Query("search") search: String?,
        @Query("purchased") purchased: Boolean?,
        @Query("per_page") page: Int = 9999,
    ): Response<NetworkPagedItems>

    @POST("shopping-lists/{listId}/items")
    suspend fun addListItem(
        @Path("listId") listId: Int,
        @Body itemData: NetworkNewItem
    ): Response<NetworkItem>

    @PUT("shopping-lists/{listId}/items/{itemId}")
    suspend fun updateListItem(
        @Path("listId") listId: Int,
        @Path("itemId") itemId: Int,
        @Body itemData: NetworkNewItem
    ): Response<NetworkItem>

    @DELETE("shopping-lists/{listId}/items/{itemId}")
    suspend fun deleteListItem(
        @Path("listId") listId: Int,
        @Path("itemId") itemId: Int,
    ): Response<Unit>

    @PATCH("shopping-lists/{listId}/items/{itemId}")
    suspend fun setListItemPurchased(
        @Path("listId") listId: Int,
        @Path("itemId") itemId: Int,
        @Body purchased: NetworkItemPurchased
    ): Response<NetworkItem>
}