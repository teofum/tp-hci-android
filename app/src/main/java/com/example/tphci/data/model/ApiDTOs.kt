package com.example.tphci.data.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class LoginResponse(
    val token: String
)

@Serializable
data class RegisterResponse(
    val id: Int,
    val name: String,
    val surname: String,
    val email: String,
    val metadata: JsonElement,
    val updatedAt: String,
    val createdAt: String,
)

@Serializable
data class ProductsResponse(
    val data: List<Product>,
    val pagination: JsonElement
)
