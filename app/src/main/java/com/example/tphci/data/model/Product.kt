package com.example.tphci.data.model

import com.example.tphci.data.network.model.NetworkCategoryId
import com.example.tphci.data.network.model.NetworkMetadata
import com.example.tphci.data.network.model.NetworkNewProduct
import com.example.tphci.data.network.model.NetworkProduct
import java.util.Date

data class Product(
    val id: Int?,
    var name: String?,
    var emoji: String?,
    val createdAt: Date?,
    val updatedAt: Date?,
    val category: Category?
) {
    constructor(name: String?, categoryId: Int?, emoji: String = "\uD83D\uDCE6") : this(
        null, name, emoji, null, null,
        if (categoryId != null) Category(categoryId) else null
    )

    fun asNetworkNewModel(): NetworkNewProduct {
        return if (category != null)
            NetworkNewProduct(
                category = NetworkCategoryId(category.id!!),
                name = name,
                metadata = NetworkMetadata(emoji!!)
            )
        else
            NetworkNewProduct(
                name = name,
                metadata = NetworkMetadata(emoji!!)
            )
    }

    fun asNetworkModel(): NetworkProduct {
        return NetworkProduct(
            id = id!!,
            category = category?.asNetworkModel(),
            name = name,
            metadata = NetworkMetadata(emoji!!),
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}
