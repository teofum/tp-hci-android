package com.example.tphci.data.repository

import com.example.tphci.data.network.RemoteDataSource
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import com.example.tphci.data.model.User

class ShareScreenRepository(private val remoteDataSource: RemoteDataSource) {
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun getShared(): List<User> {
        val response = remoteDataSource.get("shared")// H
        val sharedJson = response.jsonObject["data"]?.jsonArray ?: return emptyList()

        return sharedJson.mapNotNull { element ->
            try {
                json.decodeFromJsonElement<User>(element)
            } catch (e: Exception) {
                null
            }
        }
    }
}

/*
TODO: la lista de compartidos esta en la llamada de cada lista
      "ShoppingList": {
        "type": "object",
        "properties": {
          "id": { "type": "integer", "format": "int64" },
          "name": { "type": "string", "maxLength": 100 },
          "description": { "type": "string" },
          "recurring": { "type": "boolean" },
          "metadata": { "type": "object"},
          "owner": { "$ref": "#/definitions/User" },
          "sharedWith": [], <----------------------------------"ACA"/////////////
          "lastPurchasedAt": { "type": "string", "format": "YYYY-mm-dd HH:MM:SS", "nullable": true },
          "createdAt": { "type": "string", "format": "YYYY-mm-dd HH:MM:SS" },
          "updatedAt": { "type": "string", "format": "YYYY-mm-dd HH:MM:SS" }
        },
 */