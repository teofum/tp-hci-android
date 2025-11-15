package com.example.tphci.data.network

import com.example.tphci.data.network.api.ProductApiService
import com.example.tphci.data.network.model.NetworkNewProduct
import com.example.tphci.data.network.model.NetworkPagedData
import com.example.tphci.data.network.model.NetworkProduct

class ProductRemoteDataSource(
    private val productApiService: ProductApiService
) : RemoteDataSource() {
    suspend fun createProduct(product: NetworkNewProduct): NetworkProduct {
        return handleApiResponse {
            productApiService.createProduct(product)
        }
    }

    suspend fun getProducts(): List<NetworkProduct> {
        val response: NetworkPagedData<NetworkProduct> = handleApiResponse {
            productApiService.getProducts()
        }

        return response.data
    }

    suspend fun updateProduct(id: Int, product: NetworkNewProduct): NetworkProduct {
        return handleApiResponse {
            productApiService.updateProduct(id, product)
        }
    }

    suspend fun deleteProduct(id: Int) {
        return handleApiResponse {
            productApiService.deleteProduct(id)
        }
    }
}