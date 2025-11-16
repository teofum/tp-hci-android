package com.example.tphci.data.network.model

import com.example.tphci.data.model.ShoppingListItem // For the asModel() function
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.Date


@Serializable
data class NetworkNewShoppingListItem(
    val name: String,
    val quantity: Int,
    val product_id: Int? = null,
    val category_id: Int? = null,
)

@Serializable
data class NetworkShoppingListItem(
    val id: Int,
    val shopping_list_id: Int,
    val name: String,
    val quantity: Int,
    val checked: Boolean,
    val product_id: Int? = null,
    val category_id: Int? = null,
    @Contextual
    val createdAt: Date,
    @Contextual
    val updatedAt: Date,
) {
    fun asModel(): ShoppingListItem {
        return ShoppingListItem(
            id = id,
            shoppingListId = shopping_list_id,
            name = name,
            quantity = quantity,
            checked = checked,
            productId = product_id,
            categoryId = category_id,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}

@Serializable
data class NetworkPagedShoppingListItems(
    override val data: List<NetworkShoppingListItem>,
    override val pagination: NetworkPaginationMetadata // Assumes this is defined
) : NetworkPagedData<NetworkShoppingListItem> // Assumes this is defined