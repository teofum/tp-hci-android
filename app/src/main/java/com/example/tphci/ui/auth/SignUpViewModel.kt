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

data class SignUpUIState(
    val name: String = "",
    val surname: String = "",
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: Error? = null,
    val isRegistered: Boolean = false
)

class SignUpViewModel(
    private val sessionManager: SessionManager,
    private val userRepository: UserRepository
) : ViewModel() {

    var uiState by mutableStateOf(SignUpUIState())
        private set

    fun updateName(name: String) {
        uiState = uiState.copy(name = name)
    }

    fun updateSurname(surname: String) {
        uiState = uiState.copy(surname = surname)
    }

    fun updateEmail(email: String) {
        uiState = uiState.copy(email = email)
    }

    fun updatePassword(password: String) {
        uiState = uiState.copy(password = password)
    }

    fun signUp() = runOnViewModelScope(
        { userRepository.register(uiState.name, uiState.surname, uiState.email, uiState.password) },
        { state, result -> state.copy(isRegistered = true) }
    )

    fun getRegisteredEmail(): String = uiState.email

    private fun <R> runOnViewModelScope(
        block: suspend () -> R,
        updateState: (SignUpUIState, R) -> SignUpUIState
    ): Job = viewModelScope.launch {
        uiState = uiState.copy(isLoading = true, error = null)
        runCatching { block() }
            .onSuccess { response ->
                uiState = updateState(uiState, response).copy(isLoading = false)
            }
            .onFailure { e ->
                uiState = uiState.copy(isLoading = false, error = handleError(e))
                Log.e(TAG, "SignUp failed", e)
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
        private const val TAG = "SignUpViewModel"

        fun provideFactory(
            sessionManager: SessionManager,
            userRepository: UserRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SignUpViewModel(sessionManager, userRepository) as T
            }
        }
    }
}