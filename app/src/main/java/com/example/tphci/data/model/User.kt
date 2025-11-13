package com.example.tphci.data.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int? = null,
    val email: String,
    val username: String? = null,
    val token: String? = null
)