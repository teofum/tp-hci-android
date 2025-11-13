package com.example.tphci.data.network

import com.example.tphci.data.model.Category
import com.example.tphci.data.model.*
import com.example.tphci.data.model.Product
import com.example.tphci.data.model.ShoppingList
import com.example.tphci.data.model.User
import kotlinx.serialization.json.JsonElement
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Url


// TODO solo el url de login está bien, resto revisar
interface ApiService {

    // wrappers genéricos como hicimos en web (usar en RemoteDataSource)

    @POST
    suspend fun post(
        @Url url: String,
        @Body body: JsonElement,
        @Header("Authorization") token: String
    ): JsonElement

    @GET
    suspend fun get(
        @Url url: String,
        @Header("Authorization") token: String
    ): JsonElement

    @PUT
    suspend fun put(
        @Url url: String,
        @Body body: JsonElement,
        @Header("Authorization") token: String
    ): JsonElement

    @DELETE
    suspend fun delete(
        @Url url: String,
        @Header("Authorization") token: String
    ): JsonElement

}