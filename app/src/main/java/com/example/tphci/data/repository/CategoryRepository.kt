package com.example.tphci.data.repository

import com.example.tphci.data.model.Category
import com.example.tphci.data.network.CategoryRemoteDataSource

class CategoryRepository(private val remoteDataSource: CategoryRemoteDataSource) {
    suspend fun createCategory(category: Category): Category {
        return remoteDataSource.createCategory(category.asNetworkNewModel()).asModel()
    }

    suspend fun getCategories(): List<Category> {
        return remoteDataSource.getCategories().map { it.asModel() }
    }

    suspend fun updateCategory(category: Category): Category {
        return remoteDataSource.updateCategory(category.id!!, category.asNetworkNewModel()).asModel()
    }

    suspend fun deleteCategory(categoryId: Int) {
        remoteDataSource.deleteCategory(categoryId)
    }
}