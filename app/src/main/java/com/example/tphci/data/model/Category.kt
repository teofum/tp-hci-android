package com.example.tphci.data.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class Category(
    val id: Int?,
    val name: String?,
    val metadata: JsonElement?,
    val createdAt: String?,
    val updatedAt: String?,
)