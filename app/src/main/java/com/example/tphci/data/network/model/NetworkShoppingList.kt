package com.example.tphci.data.network.model

import com.example.tphci.data.model.ShoppingList
import com.example.tphci.data.model.User
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class NetworkShoppingList(
    val id: Int,
    val name: String,
    val description: String,
    val recurring: Boolean,
    val metadata: NetworkMetadata,
    @Contextual
    val createdAt: Date,
    @Contextual
    val updatedAt: Date,
    @Contextual
    val lastPurchasedAt: Date? = null,
    val owner: User,
    val sharedWith: List<User>
) {
    fun asModel(): ShoppingList {
        return ShoppingList(
            id = id,
            name = name,
            description = description,
            recurring = recurring,
            emoji = metadata.emoji,
            createdAt = createdAt,
            updatedAt = updatedAt,
            lastPurchasedAt = lastPurchasedAt,
            owner = owner,
            sharedWith = sharedWith
        )
    }
}