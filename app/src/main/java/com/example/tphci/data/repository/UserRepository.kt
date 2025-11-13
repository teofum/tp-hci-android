package com.example.tphci.data.repository

import com.example.tphci.data.model.*
import com.example.tphci.data.model.User
import com.example.tphci.data.network.RemoteDataSource
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put

class UserRepository(private val remoteDataSource: RemoteDataSource) {

    private var currentUserToken: String? = null

    fun isLoggedIn(): Boolean {
        return currentUserToken != null
    }

    suspend fun login(email: String, password: String): Result<JsonObject> {
        return try {
            val credentials = buildJsonObject {
                put("email", email)
                put("password", password)
            }

            // Pedimos JsonElement y lo convertimos a JsonObject
            val responseElement = remoteDataSource.post("users/login", credentials)
            val response = responseElement.jsonObject

            // Guardar el token para futuras requests
            val token = response["token"]?.jsonPrimitive?.content
            currentUserToken = token
            remoteDataSource.token = token

            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }



    suspend fun register(name: String, surname: String, email: String, password: String): Result<JsonObject> {
        return try {
            val userInfo = buildJsonObject {
                put("name", name)
                put("surname", surname)
                put("email", email)
                put("password", password)
                put("metadata", buildJsonObject {})
            }
            val responseElement = remoteDataSource.post("users/register", userInfo)
            val response = responseElement.jsonObject
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getCurrentUserToken(): String? {
        return currentUserToken
    }
}