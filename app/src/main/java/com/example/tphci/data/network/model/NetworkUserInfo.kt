package com.example.tphci.data.network.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkRegistrationInfo(
    val name: String,
    val surname: String,
    val email: String,
    val password: String,
    val metadata: Map<String, String> = emptyMap()
)

@Serializable
data class NetworkVerificationInfo(
    val code: String
)

@Serializable
data class NetworkUpdateProfileInfo(
    val name: String,
    val surname: String,
    val metadata: Map<String, String> = emptyMap()
)

@Serializable
data class NetworkChangePasswordInfo(
    val currentPassword: String,
    val newPassword: String
)

@Serializable
data class NetworkResetPasswordInfo(
    val code: String,
    val password: String
)
