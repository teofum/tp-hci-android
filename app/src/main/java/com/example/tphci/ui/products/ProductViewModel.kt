package com.example.tphci.ui.products

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.tphci.MyApplication
import com.example.tphci.SessionManager
import com.example.tphci.data.DataSourceException
import com.example.tphci.data.model.Category
import com.example.tphci.data.model.Error
import com.example.tphci.data.model.Product
import com.example.tphci.data.repository.CategoryRepository
import com.example.tphci.data.repository.ProductRepository
import com.example.tphci.data.repository.UserRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductViewModel(
    sessionManager: SessionManager,
    private val userRepository: UserRepository,
    private val categoryRepository: CategoryRepository,
    private val productRepository: ProductRepository
) : ViewModel() {
    private val _uiState =
        MutableStateFlow(ProductUiState())
    val uiState: StateFlow<ProductUiState> = _uiState.asStateFlow()

    private var pollingJob: Job? = null
    private val pollingIntervalMs = 1000L // 1 seconds

    fun login(username: String, password: String) = runOnViewModelScope(
        { userRepository.login(username, password) },
        { state, _ -> state.copy(isAuthenticated = true) }
    )

    fun logout() = runOnViewModelScope(
        {
            stopPolling()
            userRepository.logout()
        },
        { state, _ ->
            state.copy(
                isAuthenticated = false,
                categories = emptyList(),
                products = emptyList(),
            )
        }
    )

    fun createCategory(category: Category) = runOnViewModelScope(
        { categoryRepository.createCategory(category) },
        { state, category -> state.copy(categories = state.categories + category) }
    )

    fun createProduct(product: Product) = runOnViewModelScope(
        { productRepository.createProduct(product) },
        { state, product -> state.copy(products = state.products + product) }
    )

    fun startPolling() {
        if (pollingJob?.isActive == true) return

        _uiState.update { it.copy(isPollingEnabled = true) }

        pollingJob = viewModelScope.launch {
            while (true) {
                try {
                    // Fetch categories
                    val categories = categoryRepository.getCategories()

                    _uiState.update { currentState ->
                        currentState.copy(
                            categories = categories,
                            error = null
                        )
                    }
                    Log.d(TAG, "Polling updated - Categories: ${categories.size}")
                } catch (e: Exception) {
                    _uiState.update { currentState ->
                        currentState.copy(error = handleError(e))
                    }
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

    fun manualRefresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isFetching = true, error = null) }
            try {
                val categories = categoryRepository.getCategories()

                _uiState.update { currentState ->
                    currentState.copy(
                        categories = categories,
                        isFetching = false,
                        error = null
                    )
                }
                Log.d(TAG, "Data refreshed manually")
            } catch (e: Exception) {
                _uiState.update { currentState ->
                    currentState.copy(
                        isFetching = false,
                        error = handleError(e)
                    )
                }
                Log.e(TAG, "Error refreshing data", e)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopPolling()
    }

    private fun <R> runOnViewModelScope(
        block: suspend () -> R,
        updateState: (ProductUiState, R) -> ProductUiState
    ): Job = viewModelScope.launch {
        _uiState.update { currentState -> currentState.copy(isFetching = true, error = null) }
        runCatching {
            block()
        }.onSuccess { response ->
            _uiState.update { currentState ->
                updateState(
                    currentState,
                    response
                ).copy(isFetching = false)
            }
        }.onFailure { e ->
            _uiState.update { currentState ->
                currentState.copy(
                    isFetching = false,
                    error = handleError(e)
                )
            }
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
        const val TAG = "UI Layer"

        fun provideFactory(
            application: MyApplication
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ProductViewModel(
                    application.sessionManager,
                    application.userRepository,
                    application.categoryRepository,
                    application.productRepository
                ) as T
            }
        }
    }
}
