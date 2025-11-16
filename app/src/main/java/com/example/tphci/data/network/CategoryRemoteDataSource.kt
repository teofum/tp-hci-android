package com.example.tphci.data.network

import com.example.tphci.data.network.api.CategoryApiService
import com.example.tphci.data.network.model.NetworkCategory
import com.example.tphci.data.network.model.NetworkNewCategory

class CategoryRemoteDataSource(
    private val categoryApiService: CategoryApiService
) : RemoteDataSource() {
    suspend fun createCategory(category: NetworkNewCategory): NetworkCategory {
        return handleApiResponse {
            categoryApiService.createCategory(category)
        }
    }

    suspend fun getCategories(): List<NetworkCategory> {
        val response = handleApiResponse {
            categoryApiService.getCategories()
        }

        return response.data
    }

    suspend fun updateCategory(id: Int, category: NetworkNewCategory): NetworkCategory {
        return handleApiResponse {
            categoryApiService.updateCategory(id, category)
        }
    }

    suspend fun deleteCategory(id: Int) {
        return handleApiResponse {
            categoryApiService.deleteCategory(id)
        }
    }
}