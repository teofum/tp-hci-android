package com.example.tphci.ui.products

import com.example.tphci.data.model.Category
import com.example.tphci.data.model.Product

data class ProductUiState(
    val isAuthenticated: Boolean = false,
    val isFetching: Boolean = false,
    val isPollingEnabled: Boolean = false,
    val error: Error? = null,
    val products: List<Product> = emptyList(),
    val categories: List<Category> = emptyList(),
)