package com.example.tphci.data.network.model

import com.example.tphci.data.model.Item
import kotlinx.serialization.Serializable

@Serializable
data class NetworkNewItem(
    val quantity: Int,
    val unit: String,
    val metadata: NetworkMetadata?,
    val product: NetworkProductId,
)

@Serializable
data class NetworkItem(
    val id: Long,
    val quantity: Int,
    val unit: String,
    val purchased: Boolean,
    val metadata: NetworkMetadata?,
    val createdAt: String?,
    val updatedAt: String?,
    val lastPurchasedAt: String?,
    val product: NetworkProduct
) {
    fun asModel(): Item {
        return Item(
            id = id,
            quantity = quantity,
            unit = unit,
            purchased = purchased,
            createdAt = createdAt,
            updatedAt = updatedAt,
            lastPurchasedAt = lastPurchasedAt,
            product = product.asModel()
        )
    }
}