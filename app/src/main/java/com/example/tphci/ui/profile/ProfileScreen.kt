package com.example.tphci.ui.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun ProfileScreen(
//    viewModel: HomeViewModel = viewModel(
//        factory = HomeViewModel.provideFactory(
//            (LocalContext.current.applicationContext as MyApplication).sessionManager,
//            (LocalContext.current.applicationContext as MyApplication).userRepository,
//            (LocalContext.current.applicationContext as MyApplication).shoppingRepository
//        )
//    )
) {

//    val uiState = viewModel.uiState

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Text("Perfil", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))

//        if (!uiState.isAuthenticated) {
        Text("Iniciar sesión", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = { /*viewModel.login(email, password)*/ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Iniciar sesión")
        }

//            if (uiState.error != null) {
//                Spacer(Modifier.height(8.dp))
//                Text(
//                    text = "Error: ${uiState.error.message}",
//                    color = MaterialTheme.colorScheme.error,
//                    style = MaterialTheme.typography.bodySmall
//                )
//            }
//        } else {
//            Text("Usuario autenticado correctamente", style = MaterialTheme.typography.bodyLarge)
//            Spacer(Modifier.height(8.dp))
//            Button(
//                onClick = { viewModel.logout() },
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Text("Cerrar sesión")
//            }
//        }
    }
}