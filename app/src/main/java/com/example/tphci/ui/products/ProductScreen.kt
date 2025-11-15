package com.example.tphci.ui.products

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tphci.MyApplication
import com.example.tphci.data.model.Category
import com.example.tphci.ui.home.HomeViewModel
import com.example.tphci.ui.products.components.AddProductBox
import com.example.tphci.ui.products.components.ManageCategoryBox
import com.example.tphci.ui.shopping_list.components.AddItemBox
import kotlinx.serialization.json.JsonNull

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

    LaunchedEffect(Unit) {
        viewModel.getProducts()
    }

    val uiState = viewModel.uiState

    var groupByCategory by remember { mutableStateOf(false) }

    var showAddProductScreen by remember { mutableStateOf(false) }

    val productSearch = remember { mutableStateOf("") }

    var showCategoryScreen by remember { mutableStateOf(false) }


    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddProductScreen = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.width(150.dp)
            ) {
                Text("+ Agregar Producto")
            }
        }
    ) { innerPadding ->
        Column(Modifier.fillMaxSize().padding(16.dp)) {

            Text(
                "Productos",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = productSearch.value,
                onValueChange = { productSearch.value = it },
                label = { Text("Nombre de la lista") },
                modifier = Modifier.fillMaxWidth()
            )


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { showCategoryScreen = true }
                ) {
                    Text(
                        "Administrar categorías",
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        color = MaterialTheme.colorScheme.primary
                    )
                }


                Spacer(modifier = Modifier.width(12.dp))

                Row(
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                ) {
                    Text("Agrupar por categoría ", fontSize = MaterialTheme.typography.bodyMedium.fontSize)
                    Switch(
                        checked = groupByCategory,
                        onCheckedChange = { groupByCategory = it },
                        modifier = Modifier.scale(0.8f)
                    )
                }
            }

            uiState.products.forEach { product ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(Color(0xFFF1F1F1), RoundedCornerShape(12.dp))
                    ) {
                        // TODO emoji
                    }

                    Spacer(Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        product.name?.let { Text(it, style = MaterialTheme.typography.bodyLarge) }

                        Text(
                            "${product.category}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }
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


    // TODO api, hardcoded
    val hardcodedCategories = listOf(
        Category(
            id = 1,
            name = "Alimentos",
            metadata = JsonNull,
            createdAt = "2025-01-10T12:00:00Z",
            updatedAt = "2025-01-10T12:00:00Z"
        ),
        Category(
            id = 2,
            name = "Limpieza",
            metadata = JsonNull,
            createdAt = "2025-01-11T15:30:00Z",
            updatedAt = "2025-01-11T15:30:00Z"
        )
    )

    if (showCategoryScreen) {
        CategoryScreen(
            categories = hardcodedCategories, // TODO api, hardcoded
            onClose = { showCategoryScreen = false },
            onAddCategory = { name ->
                //viewModel.addCategory(name) // TODO api
                showCategoryScreen = false
            }
        )
    }

}