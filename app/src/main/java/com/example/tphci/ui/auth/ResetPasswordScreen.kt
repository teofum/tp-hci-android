package com.example.tphci.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tphci.MyApplication

@Composable
fun ResetPasswordScreen(
    onNavigateToLogin: () -> Unit,
    viewModel: ResetPasswordViewModel = viewModel(
        factory = ResetPasswordViewModel.provideFactory(
            (LocalContext.current.applicationContext as MyApplication).sessionManager,
            (LocalContext.current.applicationContext as MyApplication).userRepository
        )
    )
) {
    val uiState = viewModel.uiState

    LaunchedEffect(uiState.resetSuccess) {
        if (uiState.resetSuccess) {
            onNavigateToLogin()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Restablecer contraseña",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "Ingresá el código que recibiste por correo y tu nueva contraseña",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = uiState.code,
            onValueChange = viewModel::updateCode,
            label = { Text("Código de verificación") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = uiState.password,
            onValueChange = viewModel::updatePassword,
            label = { Text("Nueva contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = uiState.confirmPassword,
            onValueChange = viewModel::updateConfirmPassword,
            label = { Text("Confirmar nueva contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading
        )

        val passwordsMatch = uiState.password == uiState.confirmPassword
        val allFieldsFilled = uiState.code.isNotBlank() && 
                            uiState.password.isNotBlank() && 
                            uiState.confirmPassword.isNotBlank()

        if (!passwordsMatch && uiState.confirmPassword.isNotBlank()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Las contraseñas no coinciden",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        uiState.error?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
            ) {
                Text(
                    text = "Código inválido o contraseñas no coinciden",
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { viewModel.resetPassword() },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading && allFieldsFilled && passwordsMatch
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(16.dp))
            } else {
                Text("Restablecer contraseña")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onNavigateToLogin) {
            Text("Volver al inicio de sesión")
        }
    }
}