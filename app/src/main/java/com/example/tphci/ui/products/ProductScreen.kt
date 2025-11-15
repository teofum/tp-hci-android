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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tphci.R
import com.example.tphci.MyApplication
import com.example.tphci.ui.home.HomeViewModel
import com.example.tphci.ui.products.components.AddProductBox
import com.example.tphci.ui.products.components.ManageCategoriesBox
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

    LaunchedEffect(Unit) {
        viewModel.getProducts()
    }

    val uiState = viewModel.uiState

    var groupByCategory by remember { mutableStateOf(false) }

    var showAddProductScreen by remember { mutableStateOf(false) }

    val productSearch = remember { mutableStateOf("") }

    var showManageCategoriesBox by remember { mutableStateOf(false) }


    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddProductScreen = true }
            ) {
                Text(stringResource(R.string.add_product))
            }
        }
    ) { innerPadding ->
        Column(Modifier.fillMaxSize().padding(16.dp)) {
            Text(stringResource(R.string.products), style = MaterialTheme.typography.headlineMedium)





            OutlinedTextField(
                value = productSearch.value,
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
                    modifier = Modifier.clickable { showManageCategoriesBox = true }
                ) {
                    Text(
                        stringResource(R.string.manage_categories),
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        color = MaterialTheme.colorScheme.primary
                    )
                }


                Spacer(modifier = Modifier.width(12.dp))

                Row(
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                ) {
                    Text(stringResource(R.string.group_by_category) + " ", fontSize = MaterialTheme.typography.bodyMedium.fontSize)
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

    if (showManageCategoriesBox) {
        ManageCategoriesBox(
            onClose = { showManageCategoriesBox = false },
            onAddCategory = { name ->
                //viewModel.addCategory(name) // TODO api
                showManageCategoriesBox = false
            }
        )
    }

}