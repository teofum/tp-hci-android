package com.example.tphci.data.network.api

import com.example.tphci.data.network.model.NetworkNewShoppingListItem
import com.example.tphci.data.network.model.NetworkPagedShoppingListItems
import com.example.tphci.data.network.model.NetworkShoppingListItem
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ShoppingListItemsApiService {

    @GET("shopping-lists/{id}/items")
    suspend fun getListItems(
        @Path("id") listId: Int
    ): Response<NetworkPagedShoppingListItems>

    @POST("shopping-lists/{id}/items")
    suspend fun addListItem(
        @Path("id") listId: Int,
        @Body itemData: NetworkNewShoppingListItem
    ): Response<NetworkShoppingListItem>

    @PUT("shopping-lists/{id}/items/{item_id}")
    suspend fun updateListItem(
        @Path("id") listId: Int,
        @Path("item_id") itemId: Int,
        @Body itemData: NetworkNewShoppingListItem
    ): Response<NetworkShoppingListItem>

    @DELETE("shopping-lists/{id}/items/{item_id}")
    suspend fun deleteListItem(
        @Path("id") listId: Int,
        @Path("item_id") itemId: Int
    ): Response<Unit>

    @POST("shopping-lists/{id}/items/{item_id}/check")
    suspend fun checkListItem(
        @Path("id") listId: Int,
        @Path("item_id") itemId: Int
    ): Response<NetworkShoppingListItem>

    @POST("shopping-lists/{id}/items/{item_id}/uncheck")
    suspend fun uncheckListItem(
        @Path("id") listId: Int,
        @Path("item_id") itemId: Int
    ): Response<NetworkShoppingListItem>
}