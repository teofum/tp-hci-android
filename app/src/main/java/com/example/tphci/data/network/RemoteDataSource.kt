package com.example.tphci.data.network

import com.example.tphci.data.model.Category
import com.example.tphci.data.model.Product
import com.example.tphci.data.model.ShoppingList
import com.example.tphci.data.model.User

class RemoteDataSource(private val apiService: ApiService) {

    suspend fun getProducts(): List<Product> {
        return apiService.getProducts()
    }

    suspend fun getCategories(): List<Category> {
        return apiService.getCategories()
    }

    suspend fun getShoppingLists(): List<ShoppingList> {
        return apiService.getShoppingLists()
    }

    suspend fun login(credentials: Map<String, String>): User {
        return apiService.login(credentials)
    }

    suspend fun signUp(userInfo: Map<String, String>): User {
        return apiService.signUp(userInfo)
    }

    suspend fun updateUser(user: User): User {
        return apiService.updateUser(user)
    }
}