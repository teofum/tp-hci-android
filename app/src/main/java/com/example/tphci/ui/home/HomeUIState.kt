package com.example.tphci.ui.home

import com.example.tphci.data.model.Category
import com.example.tphci.data.model.Product
import com.example.tphci.data.model.ShoppingList
import com.example.tphci.data.model.User
import com.example.tphci.data.model.Error

data class HomeUIState(
    val isAuthenticated: Boolean = false,
    val isFetching: Boolean = false,
    val error: Error? = null,

    val currentUser: User? = null,
    val products: List<Product> = emptyList(),
    val categories: List<Category> = emptyList(),
    val shoppingLists: List<ShoppingList> = emptyList()
)

