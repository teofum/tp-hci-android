package com.example.tphci.ui.shareList

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.tphci.MyApplication
import com.example.tphci.R
import com.example.tphci.data.network.model.NetworkShareData
import com.example.tphci.data.repository.ShoppingListRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ShareListUiState(
    val sharedUsers: List<ShareUser> = emptyList(),
    val emailInput: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
)

class ShareListViewModel(
    private val listId: Int,
    private val repository: ShoppingListRepository,
    private val context: Context,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ShareListUiState())
    val uiState: StateFlow<ShareListUiState> = _uiState.asStateFlow()

    init {
        loadSharedUsers()
    }

    private fun loadSharedUsers() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val list = repository.getList(listId)
                val sharedUsers = (list.sharedWith ?: emptyList()).map { user ->
                    ShareUser(
                        id = user.id,
                        name = user.name,
                        surname = user.surname,
                        email = user.email,
                        metadata = Unit,
                        createdAt = "",
                        updatedAt = ""
                    )
                }
                _uiState.update {
                    it.copy(
                        sharedUsers = sharedUsers,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        error = context.getString(R.string.failed_to_load_shared_users, e.message),
                        isLoading = false
                    )
                }
            }
        }
    }

    fun onEmailInputChange(email: String) {
        _uiState.update { it.copy(emailInput = email) }
    }

    fun onAddEmail() {
        val email = _uiState.value.emailInput.trim()
        if (email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                repository.shareList(listId, NetworkShareData(email))
                _uiState.update { it.copy(emailInput = "") }
                loadSharedUsers()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        error = context.getString(R.string.failed_to_update_sharing, e.message),
                        isLoading = false
                    )
                }
            }
        }
    }

    fun onRemoveSharedUser(user: ShareUser) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                repository.unshareList(listId, user.id)
                loadSharedUsers()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        error = context.getString(R.string.failed_to_update_sharing, e.message),
                        isLoading = false
                    )
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    companion object {
        fun provideFactory(
            application: MyApplication,
            listId: Int
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                require(modelClass.isAssignableFrom(ShareListViewModel::class.java))
                return ShareListViewModel(listId, application.shoppingListRepository, application) as T
            }
        }
    }
}
