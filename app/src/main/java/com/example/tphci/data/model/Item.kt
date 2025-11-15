package com.example.tphci.data.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class Item(
    val id: Long,
    val quantity: Int,
    val unit: String,
    val purchased: Boolean,
    val metadata: JsonElement?,
    val createdAt: String?,
    val updatedAt: String?,
    val lastPurchasedAt: String?,
    val product: Product
)
