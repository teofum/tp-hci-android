package com.example.tphci.ui.products

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ProductScreen(
//    viewModel: HomeViewModel = viewModel(
//        factory = HomeViewModel.provideFactory(
//            (LocalContext.current.applicationContext as MyApplication).sessionManager,
//            (LocalContext.current.applicationContext as MyApplication).userRepository,
//            (LocalContext.current.applicationContext as MyApplication).shoppingRepository
//        )
//    )
) {

//    val uiState = viewModel.uiState


    Column(Modifier
        .fillMaxSize()
        .padding(16.dp)) {
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
//            viewModel.addProduct(name = name, categoryId = categoryId)
        }) {
            Text("Agregar producto")
        }


        // productos
        Spacer(modifier = Modifier.height(16.dp))

//        Button(onClick = { viewModel.getProducts() }) {
//            Text("Obtener productos")
//        }
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        Text("Productos empty: ${uiState.products.isEmpty()}")
//
//        Text("Productos:")
//
//        uiState.products.forEach { product ->
//            Text("- ${product.name}")
//        }
    }
}