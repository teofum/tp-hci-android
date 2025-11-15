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

data class ResetPasswordUIState(
    val code: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val error: Error? = null,
    val resetSuccess: Boolean = false
)

class ResetPasswordViewModel(
    private val sessionManager: SessionManager,
    private val userRepository: UserRepository
) : ViewModel() {

    var uiState by mutableStateOf(ResetPasswordUIState())
        private set

    fun updateCode(code: String) {
        uiState = uiState.copy(code = code)
    }

    fun updatePassword(password: String) {
        uiState = uiState.copy(password = password)
    }

    fun updateConfirmPassword(password: String) {
        uiState = uiState.copy(confirmPassword = password)
    }

    fun resetPassword() = runOnViewModelScope(
        { userRepository.resetPassword(uiState.code, uiState.password) },
        { state, result -> state.copy(resetSuccess = true) }
    )

    private fun <R> runOnViewModelScope(
        block: suspend () -> R,
        updateState: (ResetPasswordUIState, R) -> ResetPasswordUIState
    ): Job = viewModelScope.launch {
        uiState = uiState.copy(isLoading = true, error = null)
        runCatching { block() }
            .onSuccess { response ->
                uiState = updateState(uiState, response).copy(isLoading = false)
            }
            .onFailure { e ->
                uiState = uiState.copy(isLoading = false, error = handleError(e))
                Log.e(TAG, "Reset password failed", e)
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
        private const val TAG = "ResetPasswordViewModel"

        fun provideFactory(
            sessionManager: SessionManager,
            userRepository: UserRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ResetPasswordViewModel(sessionManager, userRepository) as T
            }
        }
    }
}