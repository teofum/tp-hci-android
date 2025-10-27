package com.example.tphci.data.repository

import com.example.tphci.data.model.User
import com.example.tphci.data.network.RemoteDataSource

class UserRepository(private val remoteDataSource: RemoteDataSource) {

    private var currentUser: User? = null

    fun isLoggedIn(): Boolean {
        return currentUser != null
    }

    suspend fun login(username: String, password: String): Result<User> {
        return try {
            val credentials = mapOf("username" to username, "password" to password)
            val user = remoteDataSource.login(credentials)
            currentUser = user
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signUp(username: String, email: String, password: String): Result<User> {
        return try {
            val userInfo = mapOf("username" to username, "email" to email, "password" to password)
            val user = remoteDataSource.signUp(userInfo)
            currentUser = user
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateUser(user: User): Result<User> {
        return try {
            val updatedUser = remoteDataSource.updateUser(user)
            currentUser = updatedUser
            Result.success(updatedUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getCurrentUser(): User? {
        return currentUser
    }
}