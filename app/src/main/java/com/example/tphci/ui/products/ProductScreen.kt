package com.example.tphci.ui.products

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tphci.MyApplication
import com.example.tphci.ui.home.HomeViewModel
import com.example.tphci.ui.products.components.AddProductBox
import com.example.tphci.ui.shopping_list.components.AddItemBox

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

    var groupByCategory by remember { mutableStateOf(false) }

    var showAddProductScreen by remember { mutableStateOf(false) }

    val productSearch = remember { mutableStateOf("") }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddProductScreen = true }
            ) {
                Text("+ Producto")
            }
        }
    ) { innerPadding ->
        Column(Modifier.fillMaxSize().padding(16.dp)) {
            Text("Productos", style = MaterialTheme.typography.headlineMedium)


            // agregar producto
            val productName = remember { mutableStateOf("") }
            val categoryIdInput = remember { mutableStateOf("") }



            OutlinedTextField(
                value = productSearch.value,
                onValueChange = { productSearch.value = it },
                label = { Text("Nombre de la lista") },
                modifier = Modifier.fillMaxWidth()
            )


            Row(
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
            ) {
                Text(
                    "Agrupar por categoría ",
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize
                )
                Switch(
                    checked = groupByCategory,
                    onCheckedChange = { groupByCategory = it },
                    modifier = Modifier.scale(0.8f)
                )
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

    if (showAddProductScreen) {
        AddProductBox(
            onClose = { showAddProductScreen = false },
            onAdd = { name, categoryId ->
                viewModel.addProduct(name, categoryId)
                showAddProductScreen = false
            }
        )
    }
}