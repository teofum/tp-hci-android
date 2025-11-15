package com.example.tphci.data.network.model

import kotlinx.serialization.Serializable

@Serializable
sealed interface NetworkPaginationMetadata {
    val total: Int
    val page: Int
    val per_page: Int
    val total_pages: Int
    val has_next: Boolean
    val has_prev: Boolean
}

@Serializable
sealed interface NetworkPagedData<T : @Serializable Any> {
    val data: List<T>
    val pagination: NetworkPaginationMetadata
}