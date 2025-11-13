package com.example.tphci.data.network

import com.example.tphci.data.model.Category
import com.example.tphci.data.model.*
import com.example.tphci.data.model.Product
import com.example.tphci.data.model.ShoppingList
import com.example.tphci.data.model.User
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.encodeToJsonElement

class RemoteDataSource(val apiService: ApiService) {
    val json = Json { ignoreUnknownKeys = true }

    // TODO hardcode (debería empezar en null)
    var token: String? = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOjIsImlhdCI6MTc2MzAwNTgwNDg4MCwiZXhwIjoxNzYzMDA4Mzk2ODgwfQ.WNdjIMjLUNBDyfK7zObO4O7pIpAl-lFK1ludR2ncxNQ"

    suspend fun post(endpoint: String, body: JsonElement): JsonElement {
        return apiService.post(endpoint, body, "Bearer ${token.orEmpty()}")
    }

    suspend fun put(endpoint: String, body: JsonElement): JsonElement {
        return apiService.put(endpoint, body, "Bearer ${token.orEmpty()}")
    }

    suspend fun get(endpoint: String): JsonElement {
        return apiService.get(endpoint, "Bearer ${token.orEmpty()}")
    }

    suspend fun delete(endpoint: String) {
        apiService.delete(endpoint, "Bearer ${token.orEmpty()}")
    }
}