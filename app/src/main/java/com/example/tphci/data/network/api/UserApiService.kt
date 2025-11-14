package com.example.tphci.data.network.api

import com.example.tphci.data.model.User
import com.example.tphci.data.network.model.NetworkCredentials
import com.example.tphci.data.network.model.NetworkToken
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

interface UserApiService {
    @POST("users/login")
    suspend fun login(@Body credentials: NetworkCredentials): Response<NetworkToken>

    @POST("users/logout")
    suspend fun logout(): Response<Unit>

    @POST("signup")
    suspend fun signUp(@Body userInfo: Map<String, String>): User

    @PUT("user")
    suspend fun updateUser(@Body user: User): User
}