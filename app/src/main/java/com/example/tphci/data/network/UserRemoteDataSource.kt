package com.example.tphci.data.network

import com.example.tphci.SessionManager
import com.example.tphci.data.model.User
import com.example.tphci.data.network.api.UserApiService
import com.example.tphci.data.network.model.NetworkChangePasswordInfo
import com.example.tphci.data.network.model.NetworkCredentials
import com.example.tphci.data.network.model.NetworkRegistrationInfo
import com.example.tphci.data.network.model.NetworkResetPasswordInfo
import com.example.tphci.data.network.model.NetworkUpdateProfileInfo
import com.example.tphci.data.network.model.NetworkVerificationInfo

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

    suspend fun register(registrationInfo: NetworkRegistrationInfo): User {
        return handleApiResponse { userApiService.register(registrationInfo) }
    }

    suspend fun verifyEmail(verificationInfo: NetworkVerificationInfo): User {
        return handleApiResponse { userApiService.verifyAccount(verificationInfo) }
    }

    suspend fun sendVerification(email: String): NetworkVerificationInfo {
        return handleApiResponse { userApiService.sendVerification(email) }
    }

    suspend fun getCurrentUser(): User {
        return handleApiResponse { userApiService.getCurrentUser() }
    }

    suspend fun updateProfile(updateProfileInfo: NetworkUpdateProfileInfo): User {
        return handleApiResponse { userApiService.updateProfile(updateProfileInfo) }
    }

    suspend fun changePassword(changePasswordInfo: NetworkChangePasswordInfo) {
        handleApiResponse { userApiService.changePassword(changePasswordInfo) }
    }

    suspend fun forgotPassword(email: String) {
        handleApiResponse { userApiService.forgotPassword(email) }
    }

    suspend fun resetPassword(resetPasswordInfo: NetworkResetPasswordInfo) {
        handleApiResponse { userApiService.resetPassword(resetPasswordInfo) }
    }
}