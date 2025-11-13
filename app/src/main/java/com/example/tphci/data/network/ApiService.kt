package com.example.tphci.data.network

import com.example.tphci.data.model.Category
import com.example.tphci.data.model.*
import com.example.tphci.data.model.Product
import com.example.tphci.data.model.ShoppingList
import com.example.tphci.data.model.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT


// TODO solo el url de login está bien, resto revisar
interface ApiService {
    @GET("products")
    suspend fun getProducts(): List<Product>

    @GET("categories")
    suspend fun getCategories(): List<Category>

    @GET("shopping-lists")
    suspend fun getShoppingLists(): List<ShoppingList>

    @POST("users/login")
    suspend fun login(@Body credentials: Map<String, String>): LoginResponse

    @POST("users/register")
    suspend fun register(@Body userInfo: Map<String, String>): RegisterResponse

    @PUT("user")
    suspend fun updateUser(@Body user: User): User
}