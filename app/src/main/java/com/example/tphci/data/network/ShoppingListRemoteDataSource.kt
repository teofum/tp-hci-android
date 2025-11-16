package com.example.tphci.data.network

import com.example.tphci.data.network.api.ShoppingListApiService
import com.example.tphci.data.network.model.NetworkNewShoppingList
import com.example.tphci.data.network.model.NetworkPurchaseShoppingList
import com.example.tphci.data.network.model.NetworkShoppingList

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
}