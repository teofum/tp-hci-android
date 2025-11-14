package com.example.tphci.data.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int? = null,
    val email: String,
    val username: String? = null,
    val token: String? = null,
    val avatarRes: Int? = null, // aca deberia ir la foto o algo
)

/*
TODO:
esto es laversion salida de la api
@Serializable
data class User(
    val id: Int? = null,
    val name: string? = null,
    val surname: string? = null,
    val email: String,
    val metadata: Int? = null, // aca deberia ir la foto o algo
)
 */