package com.example.tphci.data.network.model

import com.example.tphci.data.model.Product
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class NetworkNewProduct(
    var name: String?,
    var category: NetworkCategoryId? = null,
    var metadata: NetworkMetadata? = null
)

@Serializable
class NetworkProduct(
    val id: Int,
    var name: String?,
    var category: NetworkCategory? = null,
    var metadata: NetworkMetadata? = null,
    @Contextual
    var createdAt: Date? = null,
    @Contextual
    var updatedAt: Date? = null
) {
    fun asModel(): Product {
        return Product(
            id = id,
            name = name,
            category = category?.asModel(),
            emoji = metadata?.emoji,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}
