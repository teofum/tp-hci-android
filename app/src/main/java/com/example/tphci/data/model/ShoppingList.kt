package com.example.tphci.data.model

data class ShoppingList(
    val id: String,
    val name: String,
    val items: List<Product>
)