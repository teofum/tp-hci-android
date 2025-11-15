package com.example.tphci.data.model

import com.example.tphci.data.network.model.NetworkMetadata
import com.example.tphci.data.network.model.NetworkShoppingList
import java.util.Date

data class ShoppingList(
    val id: Int,
    var name: String,
    var description: String,
    var recurring: Boolean,
    var emoji: String,
    val createdAt: Date,
    val updatedAt: Date,
    val lastPurchasedAt: Date?,
    val owner: User,
    val sharedWith: List<User>
) {
    fun asNetworkModel(): NetworkShoppingList {
        return NetworkShoppingList(
            id = id,
            name = name,
            description = description,
            recurring = recurring,
            metadata = NetworkMetadata(emoji),
            createdAt = createdAt,
            updatedAt = updatedAt,
            lastPurchasedAt = lastPurchasedAt,
            owner = owner,
            sharedWith = sharedWith
        )
    }
}