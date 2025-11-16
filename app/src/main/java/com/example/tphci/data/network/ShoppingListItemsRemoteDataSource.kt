package com.example.tphci.data.network

import com.example.tphci.data.network.api.ShoppingListItemsApiService
import com.example.tphci.data.network.model.NetworkNewShoppingListItem
import com.example.tphci.data.network.model.NetworkShoppingListItem

class ShoppingListItemsRemoteDataSource(
    private val shoppingListItemsApiService: ShoppingListItemsApiService
) : RemoteDataSource() {

    suspend fun getListItems(listId: Int): List<NetworkShoppingListItem> {
        val response = handleApiResponse {
            shoppingListItemsApiService.getListItems(listId)
        }
        return response.data
    }

    suspend fun addListItem(listId: Int, itemData: NetworkNewShoppingListItem): NetworkShoppingListItem {
        return handleApiResponse {
            shoppingListItemsApiService.addListItem(listId, itemData)
        }
    }

    suspend fun updateListItem(
        listId: Int,
        itemId: Int,
        itemData: NetworkNewShoppingListItem
    ): NetworkShoppingListItem {
        return handleApiResponse {
            shoppingListItemsApiService.updateListItem(listId, itemId, itemData)
        }
    }

    suspend fun deleteListItem(listId: Int, itemId: Int) {
        handleApiResponse {
            shoppingListItemsApiService.deleteListItem(listId, itemId)
        }
    }

    suspend fun checkListItem(listId: Int, itemId: Int): NetworkShoppingListItem {
        return handleApiResponse {
            shoppingListItemsApiService.checkListItem(listId, itemId)
        }
    }

    suspend fun uncheckListItem(listId: Int, itemId: Int): NetworkShoppingListItem {
        return handleApiResponse {
            shoppingListItemsApiService.uncheckListItem(listId, itemId)
        }
    }
}