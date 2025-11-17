package com.example.tphci.ui.shareList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.tphci.MyApplication
import com.example.tphci.data.network.model.NetworkShareData
import com.example.tphci.data.repository.ShoppingListRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ShareListUiState(
    val selectedUsers: List<ShareUser> = emptyList(),
    // val suggestedUsers: List<ShareUser> = emptyList(), // Removed
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSharingSuccessful: Boolean = false,
)

class ShareListViewModel(
    private val listId: Int,
    private val repository: ShoppingListRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ShareListUiState())
    val uiState: StateFlow<ShareListUiState> = _uiState.asStateFlow()

    init {
        loadSharedUsers()
        // loadInitialSuggestions() // Removed
    }

    private fun loadSharedUsers() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val sharedUsers = repository.getSharedUsers(listId)
                _uiState.update {
                    it.copy(
                        selectedUsers = sharedUsers,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        error = "Failed to load shared users: ${e.message}",
                        isLoading = false
                    )
                }
            }
        }
    }

    /*
    private fun loadInitialSuggestions() {
        val mockSuggestions = listOf(
            ShareUser(101, "Alice", "Smith", "alice@example.com", Unit, "", ""),
            ShareUser(102, "Bob", "Johnson", "bob@example.com", Unit, "", ""),
            ShareUser(103, "Charlie", "Brown", "charlie@example.com", Unit, "", ""),
        ).filter { user ->
            _uiState.value.selectedUsers.none { it.id == user.id }
        }
        _uiState.update { it.copy(suggestedUsers = mockSuggestions) }
    }
    */


    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }

        /*
        if (query.length > 2) {
            val filteredSuggestions = listOf(
                ShareUser(201, "Dave", "Lee", "dave.lee@work.com", Unit, "", ""),
                ShareUser(202, "Diana", "Prince", "diana.p@hero.com", Unit, "", ""),
                ShareUser(203, "David", "Miller", "david.m@home.com", Unit, "", "")
            ).filter { user ->
                user.fullName.contains(query, ignoreCase = true) ||
                        user.email.contains(query, ignoreCase = true)
            }.filter { user ->
                _uiState.value.selectedUsers.none { it.id == user.id }
            }
            _uiState.update { it.copy(suggestedUsers = filteredSuggestions) }
        } else {
            loadInitialSuggestions()
        }
        */
    }

    fun onShareUserToggle(user: ShareUser) {
        val selected = _uiState.value.selectedUsers
        // val suggested = _uiState.value.suggestedUsers // Suggested users no longer exist in state

        if (selected.contains(user)) {
            _uiState.update {
                it.copy(
                    selectedUsers = selected.filter { it.id != user.id },
                    // suggestedUsers logic removed:
                    // suggestedUsers = if (suggested.none { it.id == user.id }) suggested + user else suggested
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    selectedUsers = selected + user,
                    // suggestedUsers logic removed:
                    // suggestedUsers = suggested.filter { it.id != user.id }
                )
            }
        }
    }


    fun onRemoveSelectedShareUser(user: ShareUser) {
        val selected = _uiState.value.selectedUsers
        _uiState.update {
            it.copy(
                selectedUsers = selected.filter { it.id != user.id },
            )
        }
    }

    fun onDoneClick() {
        _uiState.update { it.copy(isLoading = true, error = null, isSharingSuccessful = false) }

        viewModelScope.launch {
            try {
                val initialSharedUsers = repository.getSharedUsers(listId)
                val currentSelectedUsers = _uiState.value.selectedUsers.toSet()

                val usersToShare = currentSelectedUsers.filter { user ->
                    initialSharedUsers.none { it.id == user.id }
                }

                val usersToUnshare = initialSharedUsers.filter { user ->
                    currentSelectedUsers.none { it.id == user.id }
                }

                for (user in usersToShare) {
                    val shareData = NetworkShareData(
                        email = user.email
                    )
                    repository.shareList(listId, shareData)
                }

                for (user in usersToUnshare) {
                    repository.unshareList(listId, user.id)
                }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isSharingSuccessful = true
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        error = "Failed to update sharing: ${e.message}",
                        isLoading = false
                    )
                }
            }
        }
    }

    companion object {
        fun provideFactory(
            application: MyApplication,
            listId: Int
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                require(modelClass.isAssignableFrom(ShareListViewModel::class.java))
                return ShareListViewModel(listId, application.shoppingListRepository) as T
            }
        }
    }
}