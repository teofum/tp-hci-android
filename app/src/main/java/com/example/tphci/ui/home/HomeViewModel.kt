package com.example.tphci.ui.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.tphci.SessionManager
import com.example.tphci.data.model.*
import com.example.tphci.data.model.Error
import com.example.tphci.data.repository.ShoppingRepository
import com.example.tphci.data.repository.UserRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.tphci.data.DataSourceException
import com.example.tphci.data.model.User

class HomeViewModel(
    private val sessionManager: SessionManager,
    private val userRepository: UserRepository,
    private val shoppingRepository: ShoppingRepository
) : ViewModel() {

    var uiState by mutableStateOf(HomeUIState(isAuthenticated = sessionManager.loadAuthToken() != null))
        private set


    fun login(email: String, password: String) = runOnViewModelScope<Result<LoginResponse>>(
        { userRepository.login(email, password) },
        { state, result ->
            result.fold(
                onSuccess = { response ->
                    sessionManager.saveAuthToken(response.token)
                    state.copy(isAuthenticated = true, currentUserToken = response.token)
                },
                onFailure = { e ->
                    state.copy(error = handleError(e), isAuthenticated = false)
                }
            )
        }
    )

    // TODO
//    fun register(username: String, email: String, password: String) = runOnViewModelScope<Result<RegisterResponse>>(
//        { userRepository.register(username, email, password) },
//        { state, result ->
//            result.fold(
//                onSuccess = { response ->
//                    sessionManager.saveAuthToken(response.token)
//                    state.copy(isAuthenticated = true)
//                },
//                onFailure = { e ->
//                    state.copy(error = handleError(e))
//                }
//            )
//        }
//    )

    fun getProducts() = runOnViewModelScope(
        { shoppingRepository.getProducts() },
        { state, products -> state.copy(products = products) }
    )

    fun getCategories() = runOnViewModelScope(
        { shoppingRepository.getCategories() },
        { state, categories -> state.copy(categories = categories) }
    )

    fun getShoppingLists() = runOnViewModelScope(
        { shoppingRepository.getShoppingLists() },
        { state, lists -> state.copy(shoppingLists = lists) }
    )

    fun logout() {
        sessionManager.removeAuthToken()
        uiState = HomeUIState(isAuthenticated = false)
    }

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
