package com.example.tphci.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tphci.MyApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonObject

// TODO este seríá la estructura del proyecto

@Composable
fun HomeScreen(
    onLogout: () -> Unit,
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

        if (uiState.isAuthenticated) {


            Text("User login ok")
            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = { 
                viewModel.logout()
                onLogout()
            }) {
                Text("Cerrar sesión")
            }



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



            // agregar lista
            val listName = remember { mutableStateOf("") }
            val listDescription = remember { mutableStateOf("") }
            val recurring = remember { mutableStateOf(false) }


            OutlinedTextField(
                value = listName.value,
                onValueChange = { listName.value = it },
                label = { Text("Nombre de la lista") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = listDescription.value,
                onValueChange = { listDescription.value = it },
                label = { Text("Descriptión de la lista") },
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Recurrente")
                Switch(
                    checked = recurring.value,
                    onCheckedChange = { recurring.value = it },
                    colors = SwitchDefaults.colors(checkedThumbColor = Color.Green)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = {
                viewModel.addShoppingList(
                    name = listName.value,
                    description = listDescription.value,
                    recurring = recurring.value
                )
            }) {
                Text("Agregar lista")
            }


            // listas
            Button(onClick = { viewModel.getShoppingLists() }) {
                Text("Obtener listas")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text("Listas empty: ${uiState.shoppingLists.isEmpty()}")

            Text("Listas:")

            uiState.shoppingLists.forEach { list ->
                Text("- ${list.name}")
            }



            Spacer(modifier = Modifier.height(16.dp))

            // TODO
//            Button(onClick = { viewModel.getCategories() }) {
//                Text("Obtener categorías")
//            }

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
            viewModel.getShoppingLists()
//             viewModel.getCategories() // TODO
        }
    }
}
