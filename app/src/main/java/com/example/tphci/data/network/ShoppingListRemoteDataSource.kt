package com.example.tphci.data.network

import com.example.tphci.data.network.api.ShoppingListApiService
import com.example.tphci.data.network.model.NetworkNewShoppingList
import com.example.tphci.data.network.model.NetworkPurchaseShoppingList
import com.example.tphci.data.network.model.NetworkShareData
import com.example.tphci.data.network.model.NetworkShoppingList
import com.example.tphci.ui.shareList.ShareUser

class ShoppingListRemoteDataSource(
    private val shoppingListApiService: ShoppingListApiService
) : RemoteDataSource() {
    suspend fun createList(listData: NetworkNewShoppingList): NetworkShoppingList {
        return handleApiResponse {
            shoppingListApiService.createList(listData)
        }
    }

    suspend fun getLists(): List<NetworkShoppingList> {
        val response = handleApiResponse {
            shoppingListApiService.getLists()
        }
        return response.data
    }

    suspend fun getList(id: Int): NetworkShoppingList {
        return handleApiResponse {
            shoppingListApiService.getList(id)
        }
    }

    suspend fun updateList(id: Int, listData: NetworkNewShoppingList): NetworkShoppingList {
        return handleApiResponse {
            shoppingListApiService.updateList(id, listData)
        }
    }

    suspend fun deleteList(id: Int) {
        handleApiResponse {
            shoppingListApiService.deleteList(id)
        }
    }

    suspend fun purchaseList(id: Int, data: NetworkPurchaseShoppingList): NetworkShoppingList {
        return handleApiResponse {
            shoppingListApiService.purchaseList(id, data)
        }
    }

    suspend fun share(listId: Int, shareData: NetworkShareData) {
        handleApiResponse {
            shoppingListApiService.share(listId, shareData)
        }
    }

    suspend fun sharedUsers(listId: Int): List<ShareUser> {
        return handleApiResponse {
            shoppingListApiService.sharedUsers(listId)
        }
    }

    suspend fun unshare(listId: Int, userId: Int) {
        handleApiResponse {
            shoppingListApiService.unshare(listId, userId)
        }
    }
}