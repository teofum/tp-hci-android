package com.example.tphci.data.network.api

import com.example.tphci.data.model.User
import com.example.tphci.data.network.model.NetworkChangePasswordInfo
import com.example.tphci.data.network.model.NetworkCredentials
import com.example.tphci.data.network.model.NetworkRegistrationInfo
import com.example.tphci.data.network.model.NetworkResetPasswordInfo
import com.example.tphci.data.network.model.NetworkToken
import com.example.tphci.data.network.model.NetworkUpdateProfileInfo
import com.example.tphci.data.network.model.NetworkVerificationInfo
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface UserApiService {
    @POST("users/login")
    suspend fun login(@Body credentials: NetworkCredentials): Response<NetworkToken>

    @POST("users/logout")
    suspend fun logout(): Response<Unit>

    @POST("users/register")
    suspend fun register(@Body registrationInfo: NetworkRegistrationInfo): Response<User>

    @POST("users/verify-account")
    suspend fun verifyAccount(@Body verificationInfo: NetworkVerificationInfo): Response<User>

    @POST("users/send-verification")
    suspend fun sendVerification(@Query("email") email: String): Response<NetworkVerificationInfo>

    @GET("users/profile")
    suspend fun getCurrentUser(): Response<User>

    @PUT("users/profile")
    suspend fun updateProfile(@Body updateProfileInfo: NetworkUpdateProfileInfo): Response<User>

    @POST("users/change-password")
    suspend fun changePassword(@Body changePasswordInfo: NetworkChangePasswordInfo): Response<Unit>

    @POST("users/forgot-password")
    suspend fun forgotPassword(@Query("email") email: String): Response<Unit>

    @POST("users/reset-password")
    suspend fun resetPassword(@Body resetPasswordInfo: NetworkResetPasswordInfo): Response<Unit>
}