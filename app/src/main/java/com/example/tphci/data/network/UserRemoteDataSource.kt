package com.example.tphci.data.network

import com.example.tphci.SessionManager
import com.example.tphci.data.network.api.UserApiService
import com.example.tphci.data.network.model.NetworkCredentials

class UserRemoteDataSource(
    private val sessionManager: SessionManager,
    private val userApiService: UserApiService
) : RemoteDataSource() {
    suspend fun login(email: String, password: String) {
        val response = handleApiResponse {
            userApiService.login(NetworkCredentials(email, password))
        }
        sessionManager.saveAuthToken(response.token)
    }

    suspend fun logout() {
        handleApiResponse { userApiService.logout() }
        sessionManager.removeAuthToken()
    }
}