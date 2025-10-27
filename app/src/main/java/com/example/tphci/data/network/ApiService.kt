package com.example.tphci.data.network

import com.example.tphci.data.model.Category
import com.example.tphci.data.model.Product
import com.example.tphci.data.model.ShoppingList
import com.example.tphci.data.model.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface ApiService {
    @GET("products")
    suspend fun getProducts(): List<Product>

    @GET("categories")
    suspend fun getCategories(): List<Category>

    @GET("shopping-lists")
    suspend fun getShoppingLists(): List<ShoppingList>

    @POST("login")
    suspend fun login(@Body credentials: Map<String, String>): User

    @POST("signup")
    suspend fun signUp(@Body userInfo: Map<String, String>): User

    @PUT("user")
    suspend fun updateUser(@Body user: User): User
}