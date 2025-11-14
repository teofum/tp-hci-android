package com.example.tphci.data.repository

import com.example.tphci.data.network.UserRemoteDataSource

class UserRepository(private val remoteDataSource: UserRemoteDataSource) {
    suspend fun login(email: String, password: String) {
        remoteDataSource.login(email, password)
    }

    suspend fun logout() {
        remoteDataSource.logout();
    }
}