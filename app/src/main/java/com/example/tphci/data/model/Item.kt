package com.example.tphci.data.model

import com.example.tphci.data.network.model.NetworkItem
import com.example.tphci.data.network.model.NetworkMetadata
import com.example.tphci.data.network.model.NetworkNewItem
import com.example.tphci.data.network.model.NetworkProductId

data class Item(
    val id: Long?,
    val quantity: Int,
    val unit: String,
    val purchased: Boolean,
    val createdAt: String?,
    val updatedAt: String?,
    val lastPurchasedAt: String?,
    val product: Product
) {
    constructor(quantity: Int, unit: String, product: Product) : this(
        null,
        quantity,
        unit,
        false,
        null,
        null,
        null,
        product
    )

    fun asNetworkNewModel(): NetworkNewItem {
        return NetworkNewItem(
            quantity = quantity,
            unit = unit,
            metadata = NetworkMetadata(""),
            product = NetworkProductId(product.id!!)
        )
    }

    fun asNetworkModel(): NetworkItem {
        return NetworkItem(
            id = id!!,
            quantity = quantity,
            unit = unit,
            purchased = purchased,
            metadata = NetworkMetadata(""),
            createdAt = createdAt,
            updatedAt = updatedAt,
            lastPurchasedAt = lastPurchasedAt,
            product = product.asNetworkModel()
        )
    }
}