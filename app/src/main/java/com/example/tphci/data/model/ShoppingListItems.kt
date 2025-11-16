package com.example.tphci.data.model

import com.example.tphci.data.network.model.NetworkNewShoppingListItem
import com.example.tphci.data.network.model.NetworkShoppingListItem
import java.util.Date


data class ShoppingListItem(
    val id: Int?,
    val shoppingListId: Int,
    var name: String,
    var quantity: Int,
    var checked: Boolean,
    val productId: Int? = null,
    val categoryId: Int? = null,
    val createdAt: Date?,
    val updatedAt: Date?,
) {

    constructor(
        shoppingListId: Int,
        name: String,
        quantity: Int,
        productId: Int? = null,
        categoryId: Int? = null
    ) : this(
        id = null,
        shoppingListId = shoppingListId,
        name = name,
        quantity = quantity,
        checked = false,
        productId = productId,
        categoryId = categoryId,
        createdAt = null,
        updatedAt = null
    )
    fun asNetworkNewModel(): NetworkNewShoppingListItem {
        return NetworkNewShoppingListItem(
            name = this.name,
            quantity = this.quantity,
            product_id = this.productId,
            category_id = this.categoryId,
        )
    }
}