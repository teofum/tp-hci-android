package com.example.tphci.data.repository

import com.example.tphci.data.model.Product
import com.example.tphci.data.network.ProductRemoteDataSource

class ProductRepository(private val remoteDataSource: ProductRemoteDataSource) {
    suspend fun createProduct(product: Product): Product {
        return remoteDataSource.createProduct(product.asNetworkNewModel()).asModel()
    }

    suspend fun getProducts(): List<Product> {
        return remoteDataSource.getProducts().map { it.asModel() }
    }

    suspend fun updateProduct(product: Product): Product {
        return remoteDataSource.updateProduct(product.id!!, product.asNetworkNewModel()).asModel()
    }

    suspend fun deleteProduct(productId: Int) {
        remoteDataSource.deleteProduct(productId)
    }
}