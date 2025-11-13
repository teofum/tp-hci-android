package com.example.tphci.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tphci.MyApplication

// TODO debug full AI
// TODO este seríá la estructura del proyecto

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(
        factory = HomeViewModel.provideFactory(
            (LocalContext.current.applicationContext as MyApplication).sessionManager,
            (LocalContext.current.applicationContext as MyApplication).userRepository,
            (LocalContext.current.applicationContext as MyApplication).shoppingRepository
        )
    )
) {
    val uiState = viewModel.uiState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        uiState.error?.let {
            Text(text = "Error: ${it.message}")
        }

        if (!uiState.isAuthenticated) {
            Text("Debug login")
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {
                // TODO hardcodeado
                // pueden ir a http://localhost:8080/docs con la API en Try it out
                // después ejecutar desde la página:
                // {
                //  "name": "test",
                //  "surname": "test",
                //  "email": "test@gmail.com",
                //  "password": "test1234",
                //  "metadata": {}
                // }
                // y después verificarlo
                viewModel.login("test@gmail.com", "test1234")
            }) {
                Text("hardcode login")
            }
        } else {


            // TODO debug full AI
            println("\n\n\nOK!!\n\n\n")

            Text("User login ok")
            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = { viewModel.logout() }) {
                Text("Cerrar sesión")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { viewModel.getProducts() }) {
                Text("Obtener productos")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text("Productos empty: ${uiState.products.isEmpty()}")

            Text("Productos:")

            uiState.products.forEach { product ->
                Text("- ${product.name}")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { viewModel.getCategories() }) {
                Text("Obtener categorías")
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text("Categorías:")

            uiState.categories.forEach { category ->
                Text("- ${category.name}")
            }
        }
    }

    LaunchedEffect(uiState.isAuthenticated) {
        if (uiState.isAuthenticated) {
            viewModel.getProducts()
            viewModel.getCategories()
        }
    }
}
