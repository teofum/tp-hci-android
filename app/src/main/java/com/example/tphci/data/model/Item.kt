package com.example.tphci.data.model

import com.example.tphci.data.network.model.NetworkItem
import com.example.tphci.data.network.model.NetworkMetadata
import com.example.tphci.data.network.model.NetworkNewItem

data class Item(
    val id: Long,
    val quantity: Int,
    val unit: String,
    val purchased: Boolean,
    val emoji: String,
    val createdAt: String?,
    val updatedAt: String?,
    val lastPurchasedAt: String?,
    val product: Product
) {
    fun asNetworkNewModel(): NetworkNewItem {
        return NetworkNewItem(
            quantity = quantity,
            unit = unit,
            metadata = NetworkMetadata(emoji),
            productId = product.id!!
        )
    }

    fun asNetworkModel(): NetworkItem {
        return NetworkItem(
            id = id,
            quantity = quantity,
            unit = unit,
            purchased = purchased,
            metadata = NetworkMetadata(emoji),
            createdAt = createdAt,
            updatedAt = updatedAt,
            lastPurchasedAt = lastPurchasedAt,
            product = product.asNetworkModel()
        )
    }
}