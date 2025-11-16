package com.example.tphci.ui.shopping_list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.tphci.MyApplication
import com.example.tphci.data.DataSourceException
import com.example.tphci.data.model.Error
import com.example.tphci.data.model.Item
import com.example.tphci.data.repository.ItemRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ItemUiState(
    val listId: Int? = null,
    val items: List<Item> = emptyList(),
    val isFetching: Boolean = false,
    val error: Error? = null
)

class ListItemsViewModel(
    private val listId: Int,
    private val repository: ItemRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ItemUiState(listId = listId))
    val uiState: StateFlow<ItemUiState> = _uiState.asStateFlow()

    init {
        loadListItems()
    }

    fun loadListItems() = runOnViewModelScope(
        block = { repository.getListItems(listId) },
        updateState = { state, items -> state.copy(items = items) }
    )

    fun addListItem(item: Item) = runOnViewModelScope(
        block = { repository.addListItem(listId, item) },
        updateState = { state, newItem -> state.copy(items = state.items + newItem) }
    )

    fun updateListItem(item: Item) = runOnViewModelScope(
        block = { repository.updateListItem(listId, item) },
        updateState = { state, updatedItem ->
            state.copy(items = state.items.map {
                if (it.id == updatedItem.id) updatedItem else it
            })
        }
    )

    fun deleteListItem(itemId: Int) = runOnViewModelScope(
        block = { repository.deleteListItem(listId, itemId) },
        updateState = { state, _ -> state.copy(items = state.items.filter { it.id.toInt() != itemId }) }
    )


    fun toggleCheckStatus(item: Item) {
        val block: suspend () -> Item = if (item.purchased) {
            { repository.uncheckListItem(listId, item.id.toInt()) }
        } else {
            { repository.checkListItem(listId, item.id.toInt()) }
        }

        runOnViewModelScope(
            block = block,
            updateState = { state, updatedItem ->
                state.copy(items = state.items.map {
                    if (it.id == updatedItem.id) updatedItem else it
                })
            }
        )
    }

    private fun <R> runOnViewModelScope(
        block: suspend () -> R,
        updateState: (ItemUiState, R) -> ItemUiState
    ): Job = viewModelScope.launch {
        _uiState.update { it.copy(isFetching = true, error = null) }
        runCatching {
            block()
        }.onSuccess { response ->
            _uiState.update { currentState ->
                updateState(currentState, response).copy(isFetching = false)
            }
        }.onFailure { e ->
            _uiState.update { it.copy(isFetching = false, error = handleError(e)) }
            Log.e(TAG, "Coroutine execution failed", e)
        }
    }

    private fun handleError(e: Throwable): Error {
        return if (e is DataSourceException) {
            Error(e.code, e.message ?: "")
        } else {
            Error(null, e.message ?: "An unknown error occurred")
        }
    }

    companion object {
        const val TAG = "ItemViewModel"

        fun provideFactory(
            application: MyApplication,
            listId: Int // Parameter to initialize the ViewModel
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val repository = application.itemRepository
                return ListItemsViewModel(listId, repository) as T
            }
        }
    }
}