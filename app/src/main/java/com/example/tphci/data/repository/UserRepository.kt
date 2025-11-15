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

    suspend fun verifyAccount(code: String): Result<JsonObject> {
        return try {
            val verificationData = buildJsonObject {
                put("code", code)
            }
            val responseElement = remoteDataSource.post("users/verify-account", verificationData)
            val response = responseElement.jsonObject
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun sendVerification(email: String): Result<JsonObject> {
        return try {
            val emailData = buildJsonObject {
                put("email", email)
            }
            val responseElement = remoteDataSource.post("users/send-verification", emailData)
            val response = responseElement.jsonObject
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCurrentUser(): Result<JsonObject> {
        return try {
            val responseElement = remoteDataSource.get("users/profile")
            val response = responseElement.jsonObject
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateProfile(name: String, surname: String): Result<JsonObject> {
        return try {
            val profileData = buildJsonObject {
                put("name", name)
                put("surname", surname)
                put("metadata", buildJsonObject {})
            }
            val responseElement = remoteDataSource.put("users/profile", profileData)
            val response = responseElement.jsonObject
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun changePassword(currentPassword: String, newPassword: String): Result<JsonObject> {
        return try {
            val passwordData = buildJsonObject {
                put("currentPassword", currentPassword)
                put("newPassword", newPassword)
            }
            val responseElement = remoteDataSource.post("users/change-password", passwordData)
            val response = responseElement.jsonObject
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun forgotPassword(email: String): Result<JsonObject> {
        return try {
            val responseElement = remoteDataSource.post("users/forgot-password?email=$email", buildJsonObject {})
            val response = responseElement.jsonObject
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun resetPassword(code: String, password: String): Result<JsonObject> {
        return try {
            val resetData = buildJsonObject {
                put("code", code)
                put("password", password)
            }
            val responseElement = remoteDataSource.post("users/reset-password", resetData)
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