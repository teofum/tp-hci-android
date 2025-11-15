package com.example.tphci.data.repository

import com.example.tphci.data.model.User
import com.example.tphci.data.network.RemoteDataSource
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.put


class ShareRepository(private val remoteDataSource: RemoteDataSource) {
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun getSharedUsers(listId: Int): List<User> {
        val response = remoteDataSource.get("shopping-lists/$listId/shared-users")
        val usersJson = response.jsonObject["data"]?.jsonArray ?: return emptyList()

        return usersJson.mapNotNull { element ->
            try {
                json.decodeFromJsonElement<User>(element)
            } catch (e: Exception) {
                null
            }
        }
    }



    suspend fun shareList(listId: Int, email: String): JsonObject {

        val body = buildJsonObject { put("email", email) }

        val response = remoteDataSource.post(
            endpoint = "shopping-lists/$listId/share",
            body = body
        )

        return response.jsonObject
    }

    suspend fun unShareList(listId: Int, userId: Int): JsonObject {

        val response = remoteDataSource.delete("shopping-lists/$listId/share/$userId")

        return response.jsonObject
    }

}