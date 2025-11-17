package com.example.tphci.data.repository

import com.example.tphci.data.model.ShoppingList
import com.example.tphci.data.network.ShoppingListRemoteDataSource
import com.example.tphci.data.network.model.NetworkPurchaseShoppingList
import com.example.tphci.data.network.model.NetworkShareData
import com.example.tphci.ui.shareList.ShareUser

class ShoppingListRepository(private val remoteDataSource: ShoppingListRemoteDataSource) {
    suspend fun createList(list: ShoppingList): ShoppingList {
        return remoteDataSource.createList(list.asNetworkNewModel()).asModel()
    }

    suspend fun getLists(): List<ShoppingList> {
        return remoteDataSource.getLists().map { it.asModel() }
    }

    suspend fun getList(listId: Int): ShoppingList {
        return remoteDataSource.getList(listId).asModel()
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

    suspend fun shareList(listId: Int, shareData: NetworkShareData) {
        remoteDataSource.share(listId, shareData)
    }

    suspend fun getSharedUsers(listId: Int): List<ShareUser> {
        return remoteDataSource.sharedUsers(listId)
    }

    suspend fun unshareList(listId: Int, userId: Int) {
        remoteDataSource.unshare(listId, userId)
    }
}