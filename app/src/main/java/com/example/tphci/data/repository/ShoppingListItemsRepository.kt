package com.example.tphci.data.repository

import com.example.tphci.data.model.ShoppingListItem
import com.example.tphci.data.network.ShoppingListItemsRemoteDataSource


class ShoppingListItemsRepository(
    private val remoteDataSource: ShoppingListItemsRemoteDataSource
) {

    suspend fun getListItems(listId: Int): List<ShoppingListItem> {
        return remoteDataSource.getListItems(listId).map { it.asModel() }
    }

    suspend fun addListItem(listId: Int, item: ShoppingListItem): ShoppingListItem {
        return remoteDataSource.addListItem(
            listId,
            item.asNetworkNewModel()
        ).asModel()
    }

    suspend fun updateListItem(listId: Int, item: ShoppingListItem): ShoppingListItem {
        return remoteDataSource.updateListItem(
            listId,
            item.id!!,
            item.asNetworkNewModel()
        ).asModel()
    }

    suspend fun deleteListItem(listId: Int, itemId: Int) {
        remoteDataSource.deleteListItem(listId, itemId)
    }

    suspend fun checkListItem(listId: Int, itemId: Int): ShoppingListItem {
        return remoteDataSource.checkListItem(listId, itemId).asModel()
    }

    suspend fun uncheckListItem(listId: Int, itemId: Int): ShoppingListItem {
        return remoteDataSource.uncheckListItem(listId, itemId).asModel()
    }
}