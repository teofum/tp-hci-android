package com.example.tphci.ui.products

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tphci.MyApplication
import com.example.tphci.data.model.Product
import com.example.tphci.data.model.Category
import com.example.tphci.data.model.Item
import com.example.tphci.data.model.Product
import com.example.tphci.ui.home.HomeViewModel
import com.example.tphci.ui.home.rememberWindowInfo
import com.example.tphci.ui.products.components.AddProductBox
import com.example.tphci.ui.products.components.ManageCategoriesBox
import com.example.tphci.ui.products.components.ManageCategoryBox
import com.example.tphci.ui.shopping_list.components.AddItemBox
import kotlinx.serialization.json.JsonNull

@Composable
fun ProductScreen(
    viewModel: ProductViewModel = viewModel(
        factory = ProductViewModel.provideFactory(
            LocalContext.current.applicationContext as MyApplication,
        )
    )
) {
    LaunchedEffect(Unit) {
        viewModel.startPolling()
    }

    val uiState = viewModel.uiState.collectAsState().value

    var groupByCategory by remember { mutableStateOf(false) }

    var showAddProductScreen by remember { mutableStateOf(false) }

    val productSearch = remember { mutableStateOf("") }

    var showCategoryScreen by remember { mutableStateOf(false) }

    fun categoryNameOf(product: Product): String =
        product.category?.name ?: "Sin categoría"

    val groupedProducts = if (groupByCategory) {
        uiState.products.groupBy { categoryNameOf(it) } // TODO API, categorización de prods
    } else null


    val windowInfo = rememberWindowInfo()
    val maxWidth = windowInfo.maxWidth
    val isTablet = windowInfo.maxWidth > 600.dp

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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = maxWidth)
                    .padding(16.dp)

        ) {

                Text(
                    "Productos",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = productSearch.value, // TODO api, buscador de prods
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
                }

                if (groupByCategory && groupedProducts != null && groupedProducts.isNotEmpty()) {

                    groupedProducts.forEach { (categoryName, productsInCategory) ->

                        Text(
                            text = categoryName,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)
                        )

                        productsInCategory.forEach { product ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .background(Color(0xFFF1F1F1), RoundedCornerShape(12.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "📦", // TODO api
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Normal
                                    )
                                }

                                Spacer(Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    product.name?.let {
                                        Text(
                                            it,
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                    }

                                    Text(
                                        "${product.category}", // TODO API, check si se accede bien
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.Gray
                                    )
                                }
                            }
                        }
                    }

                } else {
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
                                    .background(Color(0xFFF1F1F1), RoundedCornerShape(12.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "📦", // TODO api
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Normal
                                )
                            }

                            Spacer(Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                product.name?.let {
                                    Text(
                                        it,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }

                                Text(
                                    "${product.category}", // TODO API, check si se accede bien
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }
            }



        }
    }

    if (showAddProductScreen) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = if (isTablet) {
                    Modifier
                        .widthIn(max = 600.dp)
                        .heightIn(max = 500.dp)
                        .background(MaterialTheme.colorScheme.background, RoundedCornerShape(16.dp))
                        .padding(16.dp)
                } else {
                    Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(16.dp)
                }
            ) {
                AddProductBox(
                    onClose = { showAddProductScreen = false },
                    onAdd = { name, categoryId ->
                        viewModel.addProduct(name, categoryId) // TODO API, check contrato
                        showAddProductScreen = false
                    }
                )
            }
        }
        AddProductBox(
            onClose = { showAddProductScreen = false },
            onAdd = { name, categoryId ->
                viewModel.createProduct(Product(name = name, categoryId = categoryId))
                showAddProductScreen = false
            }
        )
    }


    // TODO API, hardcoded for debug !!
    // TODO eliminar, desde acá
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
    // TODO eliminar, hasta acá

    if (showCategoryScreen) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = if (isTablet) {
                    Modifier
                        .widthIn(max = 600.dp)
                        .heightIn(max = 500.dp)
                        .background(MaterialTheme.colorScheme.background, RoundedCornerShape(16.dp))
                        .padding(16.dp)
                } else {
                    Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(16.dp)
                }
            ) {
                CategoryScreen(
                    categories = hardcodedCategories, // TODO api, hardcoded
                    onClose = { showCategoryScreen = false },
                    onAddCategory = { name ->
                        //viewModel.addCategory(name) // TODO API
                        showCategoryScreen = false
                    }
                )
            }
        }
    }

}