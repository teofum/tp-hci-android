package com.example.tphci.ui.shopping_list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.tphci.MyApplication
import com.example.tphci.data.DataSourceException
import com.example.tphci.data.model.Error
import com.example.tphci.data.model.ShoppingList
import com.example.tphci.data.repository.ShoppingListRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ShoppingListViewModel(
    private val repository: ShoppingListRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ShoppingListUiState())
    val uiState: StateFlow<ShoppingListUiState> = _uiState.asStateFlow()

    private var pollingJob: Job? = null
    private val pollingIntervalMs = 5000L

    fun createShoppingList(shoppingList: ShoppingList) = runOnViewModelScope(
        { repository.createList(shoppingList) },
        { state, createdList -> state.copy(shoppingLists = state.shoppingLists + createdList) }
    )

    fun startPolling() {
        if (pollingJob?.isActive == true) return

        _uiState.update { it.copy(isPollingEnabled = true) }

        pollingJob = viewModelScope.launch {
            while (true) {
                try {
                    val shoppingLists = repository.getLists()
                    _uiState.update { it.copy(shoppingLists = shoppingLists, error = null) }
                    Log.d(TAG, "Polling updated - ShoppingLists: ${shoppingLists.size}")
                } catch (e: Exception) {
                    _uiState.update { it.copy(error = handleError(e)) }
                    Log.e(TAG, "Polling error", e)
                }
                delay(pollingIntervalMs)
            }
        }
    }

    fun stopPolling() {
        pollingJob?.cancel()
        pollingJob = null
        _uiState.update { it.copy(isPollingEnabled = false) }
        Log.d(TAG, "Polling stopped")
    }

    fun manualRefresh() = runOnViewModelScope(
        { repository.getLists() },
        { state, lists -> state.copy(shoppingLists = lists) }
    )

    override fun onCleared() {
        super.onCleared()
        stopPolling()
    }

    private fun <R> runOnViewModelScope(
        block: suspend () -> R,
        updateState: (ShoppingListUiState, R) -> ShoppingListUiState
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
            Error(null, e.message ?: "")
        }
    }

    companion object {
        const val TAG = "ShoppingListViewModel"

        fun provideFactory(
            application: MyApplication
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ShoppingListViewModel(application.shoppingListRepository) as T
            }
        }
    }
}