package com.example.tphci.data.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class ShoppingList(
    val id: Int,
    val name: String,
    val description: String,
    val recurring: Boolean,
    val metadata: JsonElement,
    val createdAt: String,
    val updatedAt: String,
    val lastPurchasedAt: String? = null,
    val owner: JsonElement,
    val sharedWith: List<JsonElement> // TODO revisar qué devuelve
)