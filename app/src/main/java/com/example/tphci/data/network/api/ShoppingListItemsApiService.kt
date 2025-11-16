package com.example.tphci.data.network.api

import com.example.tphci.data.network.model.NetworkItem
import com.example.tphci.data.network.model.NetworkNewItem
import com.example.tphci.data.network.model.NetworkPagedItems
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ShoppingListItemsApiService {

    @GET("shopping-lists/{listId}/items")
    suspend fun getListItems(
        @Path("listId") listId: Int
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

    @POST("shopping-lists/{listId}/items/{itemId}/check")
    suspend fun checkListItem(
        @Path("listId") listId: Int,
        @Path("itemId") itemId: Int
    ): Response<NetworkItem>

    @POST("shopping-lists/{listId}/items/{itemId}/uncheck")
    suspend fun uncheckListItem(
        @Path("listId") listId: Int,
        @Path("itemId") itemId: Int
    ): Response<NetworkItem>
}