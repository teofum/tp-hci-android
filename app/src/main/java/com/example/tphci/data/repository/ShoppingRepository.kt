package com.example.tphci.data.repository

import com.example.tphci.data.model.Category
import com.example.tphci.data.model.Product
import com.example.tphci.data.model.ShoppingList
import com.example.tphci.data.network.RemoteDataSource
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.put

class ShoppingRepository(private val remoteDataSource: RemoteDataSource) {
    private val json = Json { ignoreUnknownKeys = true }
    suspend fun getProducts(): List<Product> {
        val response = remoteDataSource.get("products")
        val productsJson = response.jsonObject["data"]?.jsonArray ?: return emptyList()

        return productsJson.mapNotNull { element ->
            try {
                json.decodeFromJsonElement<Product>(element)
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun addProduct(name: String, categoryId: Int?, metadata: Map<String, Any> = emptyMap()): JsonObject {
        val body = buildJsonObject {
            put("name", name)
            put("metadata", buildJsonObject {
                metadata.forEach { (key, value) ->
                    put(key, value.toString())
                }
            })
            if (categoryId != null) {
                put("category", buildJsonObject {
                    put("id", categoryId)
                })
            }
        }

        val response = remoteDataSource.post("products", body)

        return response.jsonObject
    }

    // TODO
//    suspend fun updateProduct(id: Int, name: String, categoryId: Int?, metadata: Map<String, Any> = emptyMap()): Product {
//        val body = buildJsonObject {
//            put("name", "Milk")
//            put("metadata", buildJsonObject { metadata })
//            put("category", buildJsonObject { put("id", 1) })
//        }
//
//        return remoteDataSource.put("products/$id", body)
//    }

    // TODO
//    suspend fun deleteProduct(id: Int) {
//        remoteDataSource.delete("products/$id")
//    }

    // TODO
//    suspend fun getCategories(): List<Category> {
//        val response = remoteDataSource.get("categories").jsonObject
//        val dataArray = response["data"]!!.jsonArray
//        return dataArray.map { json.decodeFromJsonElement<Category>(it) }
//    }

    // TODO
//    suspend fun getShoppingLists(): List<ShoppingList> {
//        return remoteDataSource.get("shopping-lists")
//    }
}