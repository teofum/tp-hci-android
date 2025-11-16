package com.example.tphci.ui.shopping_list

/**
 * Request model for data coming from the AddItemBox dialog.
 */
data class ShoppingListItem(
    val name: String,
    val categoryId: Int?
)