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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tphci.MyApplication

@Composable
fun ProductScreen(
    viewModel: ProductViewModel = viewModel(
        factory = ProductViewModel.provideFactory(
            LocalContext.current.applicationContext as MyApplication,
        )
    )
) {

    val uiState = viewModel.uiState


    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
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
//            viewModel.createProduct(name = name, categoryId = categoryId)
        }) {
            Text("Agregar producto")
        }


        // productos
        Spacer(modifier = Modifier.height(16.dp))

        Spacer(modifier = Modifier.height(8.dp))

        Text("Productos empty: ${uiState.collectAsState().value.products.isEmpty()}")

        Text("Productos:")

        uiState.collectAsState().value.products.forEach { product ->
            Text("- ${product.name}")
        }
    }
}