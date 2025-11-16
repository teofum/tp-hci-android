package com.example.tphci

import android.app.Application
import com.example.tphci.data.network.CategoryRemoteDataSource
import com.example.tphci.data.network.ProductRemoteDataSource
import com.example.tphci.data.network.ShoppingListRemoteDataSource
import com.example.tphci.data.network.UserRemoteDataSource
import com.example.tphci.data.network.api.RetrofitClient
import com.example.tphci.data.repository.CategoryRepository
import com.example.tphci.data.repository.ProductRepository
import com.example.tphci.data.repository.ShoppingListItemsRepository
import com.example.tphci.data.repository.ShoppingListRepository
import com.example.tphci.data.repository.UserRepository

class MyApplication : Application() {
    lateinit var sessionManager: SessionManager
    lateinit var userRepository: UserRepository
    lateinit var categoryRepository: CategoryRepository
    lateinit var productRepository: ProductRepository
    lateinit var shoppingListRepository: ShoppingListRepository
    lateinit var shopingListItemsRepository: ShoppingListItemsRepository

    override fun onCreate() {
        super.onCreate()

        sessionManager = SessionManager(this)

        val userApiService = RetrofitClient.getUserApiService(this)
        val userRemoteDataSource = UserRemoteDataSource(sessionManager, userApiService)

        val categoryApiService = RetrofitClient.getCategoryApiService(this)
        val categoryRemoteDataSource = CategoryRemoteDataSource(categoryApiService)

        val productApiService = RetrofitClient.getProductApiService(this)
        val productRemoteDataSource = ProductRemoteDataSource(productApiService)

        val shoppingListApiService = RetrofitClient.getShoppingListApiService(this)
        val shoppingListRemoteDataSource = ShoppingListRemoteDataSource(shoppingListApiService)


        userRepository = UserRepository(userRemoteDataSource)
        categoryRepository = CategoryRepository(categoryRemoteDataSource)
        productRepository = ProductRepository(productRemoteDataSource)
        shoppingListRepository = ShoppingListRepository(shoppingListRemoteDataSource)
    }
}
