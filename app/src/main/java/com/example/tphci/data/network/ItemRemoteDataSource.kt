package com.example.tphci.data.network

import com.example.tphci.data.network.api.ShoppingListItemsApiService
import com.example.tphci.data.network.model.NetworkItem
import com.example.tphci.data.network.model.NetworkNewItem

class ItemRemoteDataSource(
    private val shoppingListItemsApiService: ShoppingListItemsApiService
) : RemoteDataSource() {
    suspend fun getListItems(listId: Int): List<NetworkItem> {
        val response = handleApiResponse {
            shoppingListItemsApiService.getListItems(listId)
        }
        return response.data
    }

    suspend fun addListItem(listId: Int, itemData: NetworkNewItem): NetworkItem {
        return handleApiResponse {
            shoppingListItemsApiService.addListItem(listId, itemData)
        }
    }

    suspend fun updateListItem(listId: Int, itemId: Int, itemData: NetworkNewItem): NetworkItem {
        return handleApiResponse {
            shoppingListItemsApiService.updateListItem(listId, itemId, itemData)
        }
    }

    suspend fun deleteListItem(listId: Int, itemId: Int) {
        handleApiResponse {
            shoppingListItemsApiService.deleteListItem(listId, itemId)
        }
    }

    suspend fun checkListItem(listId: Int, itemId: Int): NetworkItem {
        return handleApiResponse {
            shoppingListItemsApiService.checkListItem(listId, itemId)
        }
    }

    suspend fun uncheckListItem(listId: Int, itemId: Int): NetworkItem {
        return handleApiResponse {
            shoppingListItemsApiService.uncheckListItem(listId, itemId)
        }
    }
}