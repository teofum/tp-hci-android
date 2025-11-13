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
import com.example.tphci.ui.auth.VerifyAccountScreen
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
                var showVerification by remember { mutableStateOf(false) }
                var verificationEmail by remember { mutableStateOf("") }

                when {
                    isAuthenticated -> {
                        HomeScreen(onLogout = { isAuthenticated = false })
                    }
                    showVerification -> {
                        VerifyAccountScreen(
                            email = verificationEmail,
                            onVerificationSuccess = { showVerification = false },
                            onNavigateToLogin = { 
                                showVerification = false
                                showSignUp = false
                            }
                        )
                    }
                    showSignUp -> {
                        SignUpScreen(
                            onSignUpSuccess = { email ->
                                verificationEmail = email
                                showVerification = true
                                showSignUp = false
                            },
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