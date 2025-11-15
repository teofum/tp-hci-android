package com.example.tphci.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.tphci.SessionManager
import com.example.tphci.data.DataSourceException
import com.example.tphci.data.model.*
import com.example.tphci.data.repository.ShoppingRepository
import com.example.tphci.data.repository.UserRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class HomeViewModel(
    private val sessionManager: SessionManager,
    private val userRepository: UserRepository,
    private val shoppingRepository: ShoppingRepository
) : ViewModel() {

//    var uiState by mutableStateOf(HomeUIState(isAuthenticated = sessionManager.loadAuthToken() != null))
//        private set


//    fun login(email: String, password: String) = runOnViewModelScope<Result<JsonObject>>(
//        { userRepository.login(email, password) },
//        { state, result ->
//            result.fold(
//                onSuccess = { response ->
//                    val token = response["token"]?.jsonPrimitive?.content.orEmpty()
//                    sessionManager.saveAuthToken(token)
//                    state.copy(isAuthenticated = true, currentUserToken = token)
//                },
//                onFailure = { e ->
//                    state.copy(error = handleError(e), isAuthenticated = false)
//                }
//            )
//        }
//    )

    // TODO
//    fun register(username: String, email: String, password: String) = runOnViewModelScope<Result<RegisterResponse>>(
//        { userRepository.register(username, email, password) },
//        { state, result ->
//            result.fold(
//                onSuccess = { _ ->
//                    // TODO
//                    state.copy(isFetching = false)
//                },
//                onFailure = { e ->
//                    state.copy(error = handleError(e))
//                }
//            )
//        }
//    )

//    fun getProducts() = runOnViewModelScope(
//        { shoppingRepository.getProducts() },
//        { state, products ->
//            state.copy(products = products.toList())
//        }
//    )


    // TODO
//    fun getCategories() = runOnViewModelScope(
//        { shoppingRepository.getCategories() },
//        { state, categories -> state.copy(categories = categories) }
//    )

    fun getShoppingLists() = runOnViewModelScope(
        { shoppingRepository.getShoppingLists() },
        { state, lists -> state.copy(shoppingLists = lists.toList()) }
    )

    fun logout() {
        sessionManager.removeAuthToken()
        uiState = HomeUIState(isAuthenticated = false)
    }

    fun addProduct(name: String, categoryId: Int?, metadata: Map<String, Any> = emptyMap()) =
        runOnViewModelScope<JsonObject>(
            { shoppingRepository.addProduct(name, categoryId, metadata) },
            { state, response ->
                val newProduct = response["data"]?.jsonArray?.firstOrNull()
                val updatedProducts = state.products.toMutableList()

                // TODO esto está medio roto, causa que no se actualice solo al agregar prod

                if (newProduct != null) {
                    val product = Product(
                        id = newProduct.jsonObject["id"]?.jsonPrimitive?.int,
                        name = newProduct.jsonObject["name"]?.jsonPrimitive?.content,
                        metadata = newProduct.jsonObject["metadata"],
                        createdAt = newProduct.jsonObject["createdAt"]?.jsonPrimitive?.content,
                        updatedAt = newProduct.jsonObject["updatedAt"]?.jsonPrimitive?.content,
                        category = newProduct.jsonObject["category"]?.jsonObject?.let { categoryJson ->
                            Category(
                                id = categoryJson["id"]?.jsonPrimitive?.int,
                                name = categoryJson["name"]?.jsonPrimitive?.content,
                                metadata = categoryJson["metadata"],
                                createdAt = categoryJson["createdAt"]?.jsonPrimitive?.content,
                                updatedAt = categoryJson["updatedAt"]?.jsonPrimitive?.content
                            )
                        } as Category
                    )
                    updatedProducts.add(product)
                }
                state.copy(
                    products = updatedProducts
                )
            }
        )

    fun addShoppingList(
        name: String,
        description: String?,
        recurring: Boolean,
        metadata: Map<String, Any> = emptyMap()
    ) =
        runOnViewModelScope<JsonObject>(
            { shoppingRepository.addShoppingList(name, description, recurring, metadata) },
            { state, response ->

                // TODO
                val returnedValue = response.jsonArray

                state.copy(
                    // TODO
                )
            }
        )

    private fun <R> runOnViewModelScope(
        block: suspend () -> R,
        updateState: (HomeUIState, R) -> HomeUIState
    ): Job = viewModelScope.launch {
        uiState = uiState.copy(isFetching = true, error = null)
        runCatching { block() }
            .onSuccess { response ->
                uiState = updateState(uiState, response).copy(isFetching = false)
            }
            .onFailure { e ->
                uiState = uiState.copy(isFetching = false, error = handleError(e))
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
        private const val TAG = "HomeViewModel"

        fun provideFactory(
            sessionManager: SessionManager,
            userRepository: UserRepository,
            shoppingRepository: ShoppingRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return HomeViewModel(sessionManager, userRepository, shoppingRepository) as T
            }
        }
    }
}
