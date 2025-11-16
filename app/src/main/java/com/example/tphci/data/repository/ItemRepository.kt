package com.example.tphci.data.repository

import com.example.tphci.data.model.Item
import com.example.tphci.data.network.ItemRemoteDataSource

class ItemRepository(private val remoteDataSource: ItemRemoteDataSource) {
    suspend fun getListItems(listId: Int, search: String?, purchased: Boolean?): List<Item> {
        return remoteDataSource.getListItems(listId, search, purchased).map { it.asModel() }
    }

    suspend fun addListItem(listId: Int, item: Item): Item {
        return remoteDataSource.addListItem(listId, item.asNetworkNewModel()).asModel()
    }

    suspend fun updateListItem(listId: Int, item: Item): Item {
        return remoteDataSource.updateListItem(listId, item.id!!.toInt(), item.asNetworkNewModel())
            .asModel()
    }

    suspend fun deleteListItem(listId: Int, itemId: Int) {
        remoteDataSource.deleteListItem(listId, itemId)
    }

    suspend fun setListItemPurchased(listId: Int, itemId: Int, purchased: Boolean): Item {
        return remoteDataSource.setListItemPurchased(listId, itemId, purchased).asModel()
    }
}