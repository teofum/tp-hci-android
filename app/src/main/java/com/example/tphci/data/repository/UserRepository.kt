package com.example.tphci.data.repository

import com.example.tphci.data.model.User
import com.example.tphci.data.network.UserRemoteDataSource
import com.example.tphci.data.network.model.NetworkChangePasswordInfo
import com.example.tphci.data.network.model.NetworkRegistrationInfo
import com.example.tphci.data.network.model.NetworkResetPasswordInfo
import com.example.tphci.data.network.model.NetworkUpdateProfileInfo
import com.example.tphci.data.network.model.NetworkVerificationInfo

class UserRepository(private val remoteDataSource: UserRemoteDataSource) {
    suspend fun login(email: String, password: String) {
        remoteDataSource.login(email, password)
    }

    suspend fun logout() {
        remoteDataSource.logout()
    }

    suspend fun register(
        name: String,
        surname: String,
        email: String,
        password: String
    ): User {
        return remoteDataSource.register(NetworkRegistrationInfo(name, surname, email, password))
    }

    suspend fun verifyAccount(code: String) {
        remoteDataSource.verifyEmail(NetworkVerificationInfo(code))
    }

    suspend fun sendVerification(email: String) {
        remoteDataSource.sendVerification(email)
    }

    suspend fun getCurrentUser(): User {
        return remoteDataSource.getCurrentUser()
    }

    suspend fun updateProfile(name: String, surname: String): User {
        return remoteDataSource.updateProfile(NetworkUpdateProfileInfo(name, surname))
    }

    suspend fun changePassword(currentPassword: String, newPassword: String) {
        remoteDataSource.changePassword(NetworkChangePasswordInfo(currentPassword, newPassword))
    }

    suspend fun forgotPassword(email: String) {
        remoteDataSource.forgotPassword(email)
    }

    suspend fun resetPassword(code: String, password: String) {
        remoteDataSource.resetPassword(NetworkResetPasswordInfo(code, password))
    }
}