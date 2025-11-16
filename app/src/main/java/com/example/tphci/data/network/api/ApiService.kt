package com.example.tphci.data.network.api

import kotlinx.serialization.json.JsonElement
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Url

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