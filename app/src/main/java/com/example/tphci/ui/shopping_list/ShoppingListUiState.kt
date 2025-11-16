package com.example.tphci.ui.shopping_list

import com.example.tphci.data.model.Error
import com.example.tphci.data.model.ShoppingList

data class ShoppingListUiState(
    val isAuthenticated: Boolean = false,
    val isFetching: Boolean = false,
    val isPollingEnabled: Boolean = false,
    val error: Error? = null,
    val shoppingLists: List<ShoppingList> = emptyList()
)