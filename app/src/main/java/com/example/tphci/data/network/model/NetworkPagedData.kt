package com.example.tphci.data.network.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkPaginationMetadata(
    val total: Int,
    val page: Int,
    val per_page: Int,
    val total_pages: Int,
    val has_next: Boolean,
    val has_prev: Boolean,
)

@Serializable
sealed interface NetworkPagedData<T : @Serializable Any> {
    val data: List<T>
    val pagination: NetworkPaginationMetadata
}

@Serializable
data class NetworkPagedCategories(
    val data: List<NetworkCategory>,
    val pagination: NetworkPaginationMetadata
)

@Serializable
data class NetworkPagedProducts(
    val data: List<NetworkProduct>,
    val pagination: NetworkPaginationMetadata
)

@Serializable
data class NetworkPagedShopingLists(
    val data: List<NetworkShoppingList>,
    val pagination: NetworkPaginationMetadata
)
