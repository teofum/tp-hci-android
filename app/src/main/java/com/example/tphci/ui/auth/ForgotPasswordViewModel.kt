package com.example.tphci.ui.auth

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.tphci.SessionManager
import com.example.tphci.data.DataSourceException
import com.example.tphci.data.model.Error
import com.example.tphci.data.repository.UserRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonObject

data class ForgotPasswordUIState(
    val email: String = "",
    val isLoading: Boolean = false,
    val error: Error? = null,
    val emailSent: Boolean = false
)

class ForgotPasswordViewModel(
    private val sessionManager: SessionManager,
    private val userRepository: UserRepository
) : ViewModel() {

    var uiState by mutableStateOf(ForgotPasswordUIState())
        private set

    fun updateEmail(email: String) {
        uiState = uiState.copy(email = email)
    }

    fun sendResetEmail() = runOnViewModelScope<Result<JsonObject>>(
        { userRepository.forgotPassword(uiState.email) },
        { state, result ->
            result.fold(
                onSuccess = { _ ->
                    state.copy(emailSent = true)
                },
                onFailure = { e ->
                    state.copy(error = handleError(e))
                }
            )
        }
    )

    private fun <R> runOnViewModelScope(
        block: suspend () -> R,
        updateState: (ForgotPasswordUIState, R) -> ForgotPasswordUIState
    ): Job = viewModelScope.launch {
        uiState = uiState.copy(isLoading = true, error = null)
        runCatching { block() }
            .onSuccess { response ->
                uiState = updateState(uiState, response).copy(isLoading = false)
            }
            .onFailure { e ->
                uiState = uiState.copy(isLoading = false, error = handleError(e))
                Log.e(TAG, "Forgot password failed", e)
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
        private const val TAG = "ForgotPasswordViewModel"

        fun provideFactory(
            sessionManager: SessionManager,
            userRepository: UserRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ForgotPasswordViewModel(sessionManager, userRepository) as T
            }
        }
    }
}