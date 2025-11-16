package com.example.tphci.data.network.api

import android.content.Context
import com.example.tphci.data.network.AuthInterceptor
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.Date

object RetrofitClient {
    // No usar localhost o la IP 127.0.0.1 porque es la interfaz de loopback
    // del emulador. La forma de salir del emulador para acceder al localhost
    // de host del mismo es usando la IP 10.0.2.2.
    private const val BASE_URL = "http://10.0.2.2:8080/api/"

    @Volatile
    private var instance: Retrofit? = null

    private fun getInstance(context: Context): Retrofit =
        instance ?: synchronized(this) {
            instance ?: buildRetrofit(context).also { instance = it }
        }

    private fun buildRetrofit(context: Context): Retrofit {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context))
            .addInterceptor(httpLoggingInterceptor)
            .build()

        val json = Json {
            ignoreUnknownKeys = true
            serializersModule = kotlinx.serialization.modules.SerializersModule {
                contextual(Date::class, DateSerializer)
            }
        }

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .client(okHttpClient)
            .build()
    }

    fun getApiService(context: Context): ApiService {
        return getInstance(context).create(ApiService::class.java)
    }

    fun getUserApiService(context: Context): UserApiService {
        return getInstance(context).create(UserApiService::class.java)
    }

    fun getCategoryApiService(context: Context): CategoryApiService {
        return getInstance(context).create(CategoryApiService::class.java)
    }

    fun getProductApiService(context: Context): ProductApiService {
        return getInstance(context).create(ProductApiService::class.java)
    }

    fun getShoppingListApiService(context: Context): ShoppingListApiService {
        return getInstance(context).create(ShoppingListApiService::class.java)
    }
}