package com.example.tphci.ui.products

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tphci.MyApplication
import com.example.tphci.ui.home.HomeViewModel

@Composable
fun ProductScreen(
    viewModel: HomeViewModel = viewModel(
        factory = HomeViewModel.provideFactory(
            (LocalContext.current.applicationContext as MyApplication).sessionManager,
            (LocalContext.current.applicationContext as MyApplication).userRepository,
            (LocalContext.current.applicationContext as MyApplication).shoppingRepository
        )
    )
) {

    val uiState = viewModel.uiState


    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Productos", style = MaterialTheme.typography.headlineMedium)




        // agregar producto
        val productName = remember { mutableStateOf("") }
        val categoryIdInput = remember { mutableStateOf("") }


        OutlinedTextField(
            value = productName.value,
            onValueChange = { productName.value = it },
            label = { Text("Nombre del producto") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = categoryIdInput.value,
            onValueChange = { categoryIdInput.value = it },
            label = { Text("ID de categoría") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            val name = productName.value
            val categoryId = categoryIdInput.value.toIntOrNull()
            viewModel.addProduct(name = name, categoryId = categoryId)
        }) {
            Text("Agregar producto")
        }



        // productos
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
    }
}