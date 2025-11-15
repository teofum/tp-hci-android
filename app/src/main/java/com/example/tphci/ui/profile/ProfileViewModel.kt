package com.example.tphci.ui.profile

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.tphci.AuthState
import com.example.tphci.SessionManager
import com.example.tphci.data.DataSourceException
import com.example.tphci.data.model.Error
import com.example.tphci.data.repository.UserRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive

data class ProfileUIState(
    val name: String = "",
    val surname: String = "",
    val email: String = "",
    val isLoading: Boolean = false,
    val error: Error? = null,
    val isAuthenticated: Boolean = false,
    val updateSuccess: Boolean = false
)

class ProfileViewModel(
    private val sessionManager: SessionManager,
    private val userRepository: UserRepository
) : ViewModel() {

    var uiState by mutableStateOf(ProfileUIState(isAuthenticated = sessionManager.loadAuthToken() != null))
        private set

    init {
        if (uiState.isAuthenticated) {
            getCurrentUser()
        }
    }

    fun updateName(name: String) {
        uiState = uiState.copy(name = name)
    }

    fun updateSurname(surname: String) {
        uiState = uiState.copy(surname = surname)
    }



    fun saveProfile() = runOnViewModelScope<Result<JsonObject>>(
        { userRepository.updateProfile(uiState.name, uiState.surname) },
        { state, result ->
            result.fold(
                onSuccess = { response ->
                    val name = response["name"]?.jsonPrimitive?.content ?: ""
                    val surname = response["surname"]?.jsonPrimitive?.content ?: ""
                    val email = response["email"]?.jsonPrimitive?.content ?: ""
                    state.copy(
                        name = name,
                        surname = surname,
                        email = email,
                        updateSuccess = true
                    )
                },
                onFailure = { e ->
                    state.copy(error = handleError(e))
                }
            )
        }
    )

    fun getCurrentUser() = runOnViewModelScope<Result<JsonObject>>(
        { userRepository.getCurrentUser() },
        { state, result ->
            result.fold(
                onSuccess = { response ->
                    val name = response["name"]?.jsonPrimitive?.content ?: ""
                    val surname = response["surname"]?.jsonPrimitive?.content ?: ""
                    val email = response["email"]?.jsonPrimitive?.content ?: ""
                    state.copy(
                        name = name,
                        surname = surname,
                        email = email
                    )
                },
                onFailure = { e ->
                    state.copy(error = handleError(e))
                }
            )
        }
    )

    fun logout() {
        sessionManager.removeAuthToken()
        AuthState.logout()
        uiState = ProfileUIState(isAuthenticated = false)
    }

    fun clearUpdateSuccess() {
        uiState = uiState.copy(updateSuccess = false)
    }

    private fun <R> runOnViewModelScope(
        block: suspend () -> R,
        updateState: (ProfileUIState, R) -> ProfileUIState
    ): Job = viewModelScope.launch {
        uiState = uiState.copy(isLoading = true, error = null)
        runCatching { block() }
            .onSuccess { response ->
                uiState = updateState(uiState, response).copy(isLoading = false)
            }
            .onFailure { e ->
                uiState = uiState.copy(isLoading = false, error = handleError(e))
                Log.e(TAG, "Profile operation failed", e)
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
        private const val TAG = "ProfileViewModel"

        fun provideFactory(
            sessionManager: SessionManager,
            userRepository: UserRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ProfileViewModel(sessionManager, userRepository) as T
            }
        }
    }
}