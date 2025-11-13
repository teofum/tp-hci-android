package com.example.tphci.data.repository

import com.example.tphci.data.model.*
import com.example.tphci.data.model.User
import com.example.tphci.data.network.RemoteDataSource

class UserRepository(private val remoteDataSource: RemoteDataSource) {

    private var currentUserToken: String? = null

    fun isLoggedIn(): Boolean {
        return currentUserToken != null
    }

    suspend fun login(email: String, password: String): Result<LoginResponse> {
        return try {
            val credentials = mapOf("email" to email, "password" to password)
            val response = remoteDataSource.login(credentials)
            println("\n\n\nOK!!\n\n\n")
            currentUserToken = response.token
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(username: String, email: String, password: String): Result<RegisterResponse> {
        return try {
            val userInfo = mapOf("username" to username, "email" to email, "password" to password)
            val user = remoteDataSource.register(userInfo)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getCurrentUserToken(): String? {
        return currentUserToken
    }
}