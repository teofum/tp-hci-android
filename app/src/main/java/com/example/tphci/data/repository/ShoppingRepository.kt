package com.example.tphci.data.repository

import com.example.tphci.data.model.Category
import com.example.tphci.data.model.Product
import com.example.tphci.data.model.ShoppingList
import com.example.tphci.data.network.RemoteDataSource

class ShoppingRepository(private val remoteDataSource: RemoteDataSource) {

    suspend fun getProducts(): List<Product> {
        return remoteDataSource.getProducts()
    }

    suspend fun getCategories(): List<Category> {
        return remoteDataSource.getCategories()
    }

    suspend fun getShoppingLists(): List<ShoppingList> {
        return remoteDataSource.getShoppingLists()
    }
}