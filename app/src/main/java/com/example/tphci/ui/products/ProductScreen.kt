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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tphci.R
import com.example.tphci.MyApplication
import com.example.tphci.data.model.Product
import com.example.tphci.ui.home.rememberWindowInfo
import com.example.tphci.ui.products.components.AddProductBox
import com.example.tphci.ui.SettingsBox
import com.example.tphci.ui.products.components.ModifyProductBox

@OptIn(ExperimentalMaterial3Api::class)
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
    var showSettingsBox by remember { mutableStateOf(false) }

    var showEditProductScreen by remember { mutableStateOf(false) }
    var editingProduct by remember { mutableStateOf<Product?>(null) }

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
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.products),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                actions = {
                    IconButton(onClick = { showSettingsBox = true }) {
                        Icon(
                            Icons.Default.Settings,
                            contentDescription = stringResource(R.string.settings)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddProductScreen = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.width(150.dp)
            ) {
                Text(stringResource(R.string.add_product))
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

                OutlinedTextField(
                    value = productSearch.value, // TODO api, buscador de prods
                    onValueChange = { productSearch.value = it },
                    label = { Text(stringResource(R.string.search_product)) },
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
                            stringResource(R.string.manage_categories),
                            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }


                    Spacer(modifier = Modifier.width(12.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            stringResource(R.string.group_by_category) + " ",
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

                                Box {
                                    var expanded by remember { mutableStateOf(false) }

                                    IconButton(onClick = { expanded = true }) {
                                        Icon(
                                            Icons.Default.MoreVert,
                                            contentDescription = "Opciones"
                                        )
                                    }

                                    DropdownMenu(
                                        expanded = expanded,
                                        onDismissRequest = { expanded = false }
                                    ) {
                                        DropdownMenuItem(
                                            text = { Text("Modificar") },
                                            leadingIcon = { Icon(Icons.Default.Edit, null) },
                                            onClick = {
                                                expanded = false
                                                editingProduct = product
                                                showEditProductScreen = true
                                            }
                                        )
                                        DropdownMenuItem(
                                            text = { Text("Eliminar") },
                                            leadingIcon = { Icon(Icons.Default.Delete, null) },
                                            onClick = {
                                                expanded = false
                                                viewModel.deleteProduct(product)
                                            }
                                        )
                                    }
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
                            Box {
                                var expanded by remember { mutableStateOf(false) }

                                IconButton(onClick = { expanded = true }) {
                                    Icon(
                                        Icons.Default.MoreVert,
                                        contentDescription = "Opciones"
                                    )
                                }

                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false }
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("Modificar") },
                                        leadingIcon = { Icon(Icons.Default.Edit, null) },
                                        onClick = {
                                            expanded = false
                                            editingProduct = product
                                            showEditProductScreen = true
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Eliminar") },
                                        leadingIcon = { Icon(Icons.Default.Delete, null) },
                                        onClick = {
                                            expanded = false
                                            viewModel.deleteProduct(product)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }


        }
    }

    if (showAddProductScreen) {
        AddProductBox(
            onClose = { showAddProductScreen = false },
            onAdd = { name, categoryId ->
                viewModel.createProduct(Product(name = name, categoryId = categoryId))
                showAddProductScreen = false
            }
        )
    }

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
                    categories = uiState.categories,
                    onClose = { showCategoryScreen = false },
                    onAddCategory = { category ->
//                        viewModel.createCategory(category)
                        showCategoryScreen = false
                    }
                )
            }
        }
    }

    if (showSettingsBox) {
        SettingsBox(
            onClose = { showSettingsBox = false }
        )
    }

    if (showEditProductScreen && editingProduct != null) {

        ModifyProductBox(
            product = editingProduct!!,
            onClose = { showEditProductScreen = false },
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            Text("Diálogo de Edición de Producto para: ${editingProduct!!.name}")
        }
    }
}