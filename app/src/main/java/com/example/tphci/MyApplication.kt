package com.example.tphci

import android.app.Application
import com.example.tphci.data.network.RemoteDataSource
import com.example.tphci.data.network.RetrofitClient
import com.example.tphci.data.repository.SettingsRepository
import com.example.tphci.data.repository.ShoppingRepository
import com.example.tphci.data.repository.UserRepository

class MyApplication : Application() {

    lateinit var sessionManager: SessionManager
    lateinit var userRepository: UserRepository
    lateinit var shoppingRepository: ShoppingRepository
    lateinit var settingsRepository: SettingsRepository

    override fun onCreate() {
        super.onCreate()

        sessionManager = SessionManager(this)

        val apiService = RetrofitClient.getApiService(this)
        val remoteDataSource = RemoteDataSource(apiService)

        userRepository = UserRepository(remoteDataSource)
        shoppingRepository = ShoppingRepository(remoteDataSource)
        settingsRepository = SettingsRepository(this)
    }
}
