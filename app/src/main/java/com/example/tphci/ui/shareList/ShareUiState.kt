package com.example.tphci.ui.shareList

import com.example.tphci.data.model.Error
import com.example.tphci.data.model.ShoppingList

data class ShareUiState(
    val isFetching: Boolean = false,
    val error: Error? = null,
    val list: ShoppingList? = null,
    val searchQuery: String = "",
    val selectedUsers: List<ShareUser> = emptyList(),
    val suggestedUsers: List<ShareUser> = emptyList(),
)