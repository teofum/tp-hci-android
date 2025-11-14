package com.example.tphci.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tphci.MyApplication

@Composable
fun SignUpScreen(
    onSignUpSuccess: (String) -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: SignUpViewModel = viewModel(
        factory = SignUpViewModel.provideFactory(
            (LocalContext.current.applicationContext as MyApplication).sessionManager,
            (LocalContext.current.applicationContext as MyApplication).userRepository
        )
    )
) {
    val uiState = viewModel.uiState

    LaunchedEffect(uiState.isRegistered) {
        if (uiState.isRegistered) {
            onSignUpSuccess(viewModel.getRegisteredEmail())
        }
    }

    AuthLayout {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Creá tu cuenta",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            OutlinedTextField(
                value = uiState.name,
                onValueChange = viewModel::updateName,
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.surname,
                onValueChange = viewModel::updateSurname,
                label = { Text("Apellido") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.email,
                onValueChange = viewModel::updateEmail,
                label = { Text("Correo electrónico") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.password,
                onValueChange = viewModel::updatePassword,
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            )

            uiState.error?.let {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                ) {
                    Text(
                        text = "Error al crear la cuenta",
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            } ?: run {
                Spacer(modifier = Modifier.height(32.dp))
            }

            Button(
                onClick = { viewModel.signUp() },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading && uiState.name.isNotBlank() && uiState.surname.isNotBlank() && uiState.email.isNotBlank() && uiState.password.isNotBlank()
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp))
                } else {
                    Text("Crear Cuenta")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = onNavigateToLogin) {
                Text("¿Ya tenés una cuenta? Iniciar sesión")
            }
        }
    }
}