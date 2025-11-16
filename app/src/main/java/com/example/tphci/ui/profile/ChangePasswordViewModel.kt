package com.example.tphci.ui.profile

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

data class ChangePasswordUIState(
    val currentPassword: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val error: Error? = null,
    val changeSuccess: Boolean = false
)

class ChangePasswordViewModel(
    private val sessionManager: SessionManager,
    private val userRepository: UserRepository
) : ViewModel() {

    var uiState by mutableStateOf(ChangePasswordUIState())
        private set

    fun updateCurrentPassword(password: String) {
        uiState = uiState.copy(currentPassword = password)
    }

    fun updateNewPassword(password: String) {
        uiState = uiState.copy(newPassword = password)
    }

    fun updateConfirmPassword(password: String) {
        uiState = uiState.copy(confirmPassword = password)
    }

    fun changePassword() = runOnViewModelScope(
        { userRepository.changePassword(uiState.currentPassword, uiState.newPassword) },
        { state, result -> state.copy(changeSuccess = true) }
    )

    fun clearChangeSuccess() {
        uiState = uiState.copy(changeSuccess = false)
    }

    private fun <R> runOnViewModelScope(
        block: suspend () -> R,
        updateState: (ChangePasswordUIState, R) -> ChangePasswordUIState
    ): Job = viewModelScope.launch {
        uiState = uiState.copy(isLoading = true, error = null)
        runCatching { block() }
            .onSuccess { response ->
                uiState = updateState(uiState, response).copy(isLoading = false)
            }
            .onFailure { e ->
                uiState = uiState.copy(isLoading = false, error = handleError(e))
                Log.e(TAG, "Change password failed", e)
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
        private const val TAG = "ChangePasswordViewModel"

        fun provideFactory(
            sessionManager: SessionManager,
            userRepository: UserRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ChangePasswordViewModel(sessionManager, userRepository) as T
            }
        }
    }
}