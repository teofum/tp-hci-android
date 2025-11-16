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

data class VerifyAccountUIState(
    val code: String = "",
    val email: String = "",
    val isLoading: Boolean = false,
    val error: Error? = null,
    val isVerified: Boolean = false
)

class VerifyAccountViewModel(
    private val sessionManager: SessionManager,
    private val userRepository: UserRepository
) : ViewModel() {

    var uiState by mutableStateOf(VerifyAccountUIState())
        private set

    fun setEmail(email: String) {
        uiState = uiState.copy(email = email)
    }

    fun updateCode(code: String) {
        uiState = uiState.copy(code = code)
    }

    fun verifyAccount() = runOnViewModelScope(
        { userRepository.verifyAccount(uiState.code) },
        { state, result -> state.copy(isVerified = true) }
    )

    fun resendCode() = runOnViewModelScope(
        { userRepository.sendVerification(uiState.email) },
        { state, result -> state.copy(error = null) }
    )

    private fun <R> runOnViewModelScope(
        block: suspend () -> R,
        updateState: (VerifyAccountUIState, R) -> VerifyAccountUIState
    ): Job = viewModelScope.launch {
        uiState = uiState.copy(isLoading = true, error = null)
        runCatching { block() }
            .onSuccess { response ->
                uiState = updateState(uiState, response).copy(isLoading = false)
            }
            .onFailure { e ->
                uiState = uiState.copy(isLoading = false, error = handleError(e))
                Log.e(TAG, "Verify account failed", e)
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
        private const val TAG = "VerifyAccountViewModel"

        fun provideFactory(
            sessionManager: SessionManager,
            userRepository: UserRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return VerifyAccountViewModel(sessionManager, userRepository) as T
            }
        }
    }
}