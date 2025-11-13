package com.example.tphci

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.tphci.ui.auth.LoginScreen
import com.example.tphci.ui.auth.SignUpScreen
import com.example.tphci.ui.home.HomeScreen
import com.example.tphci.ui.theme.TPHCITheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TPHCITheme {
                val sessionManager = (application as MyApplication).sessionManager
                var isAuthenticated by remember { mutableStateOf(sessionManager.loadAuthToken() != null) }
                var showSignUp by remember { mutableStateOf(false) }

                when {
                    isAuthenticated -> {
                        HomeScreen(onLogout = { isAuthenticated = false })
                    }
                    showSignUp -> {
                        SignUpScreen(
                            onSignUpSuccess = { showSignUp = false },
                            onNavigateToLogin = { showSignUp = false }
                        )
                    }
                    else -> {
                        LoginScreen(
                            onLoginSuccess = { isAuthenticated = true },
                            onNavigateToSignUp = { showSignUp = true }
                        )
                    }
                }
            }
        }
    }
}