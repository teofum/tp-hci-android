package com.example.tphci

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object AuthState {
    var isAuthenticated by mutableStateOf(false)
        private set
    
    fun login() {
        isAuthenticated = true
    }
    
    fun logout() {
        isAuthenticated = false
    }
    
    fun initialize(sessionManager: SessionManager) {
        isAuthenticated = sessionManager.loadAuthToken() != null
    }
}