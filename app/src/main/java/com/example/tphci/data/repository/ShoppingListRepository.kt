package com.example.tphci.data.repository

import com.example.tphci.data.model.ShoppingList
import com.example.tphci.data.network.ShoppingListRemoteDataSource
import com.example.tphci.data.network.model.NetworkPurchaseShoppingList

class ShoppingListRepository(private val remoteDataSource: ShoppingListRemoteDataSource) {
    suspend fun createList(list: ShoppingList): ShoppingList {
        return remoteDataSource.createList(list.asNetworkNewModel()).asModel()
    }

    suspend fun getLists(): List<ShoppingList> {
        return remoteDataSource.getLists().map { it.asModel() }
    }

    suspend fun updateList(list: ShoppingList): ShoppingList {
        return remoteDataSource.updateList(list.id!!, list.asNetworkNewModel()).asModel()
    }

    suspend fun deleteList(listId: Int) {
        remoteDataSource.deleteList(listId)
    }

    suspend fun purchaseList(listId: Int): ShoppingList {
        return remoteDataSource.purchaseList(listId, NetworkPurchaseShoppingList(Unit)).asModel()
    }
}