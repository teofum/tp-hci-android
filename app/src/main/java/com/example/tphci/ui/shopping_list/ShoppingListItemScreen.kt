package com.example.tphci.ui.shopping_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tphci.MyApplication
import com.example.tphci.ui.home.HomeViewModel
import com.example.tphci.ui.shopping_list.components.AddProductBox
import com.example.tphci.ui.shopping_list.components.ListItem


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListItemScreen(
    listId: Long,
    onClose: () -> Unit,
    viewModel: HomeViewModel = viewModel(
        factory = HomeViewModel.provideFactory(
            (LocalContext.current.applicationContext as MyApplication).sessionManager,
            (LocalContext.current.applicationContext as MyApplication).userRepository,
            (LocalContext.current.applicationContext as MyApplication).shoppingRepository
        )
    )
) {
    val uiState = viewModel.uiState
    val items = uiState.shoppingListItems[listId] ?: emptyList()

    val currentList = uiState.shoppingLists.firstOrNull { it.id.toLong() == listId }

    var searchQuery by remember { mutableStateOf("") }
    var filterExpanded by remember { mutableStateOf(false) }
    var selectedFilter by remember { mutableStateOf("Todos") }
    val filterOptions = listOf("Todos", "Comprados", "Pendientes")

    var groupByCategory by remember { mutableStateOf(false) }

    var showAddProductScreen by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.getShoppingLists()
    }

    LaunchedEffect(listId) {
        viewModel.getShoppingListsItems(listId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = currentList?.name ?: "",
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Compartir"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            androidx.compose.material3.FloatingActionButton(
                onClick = { showAddProductScreen = true }
            ) {
                Text("+ Agregar producto")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 8.dp),
        ) {

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Buscar producto") },
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                ExposedDropdownMenuBox(
                    expanded = filterExpanded,
                    onExpandedChange = { filterExpanded = !filterExpanded },
                    modifier = Modifier.weight(0.8f),
                ) {
                    OutlinedTextField(
                        value = selectedFilter,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Filtrar") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = filterExpanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = filterExpanded,
                        onDismissRequest = { filterExpanded = false }
                    ) {
                        filterOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    selectedFilter = option
                                    filterExpanded = false
                                }
                            )
                        }
                    }
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

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items) { item ->
                    ListItem(
                        item = item,
                        onToggle = {
                            // TODO viewModel.toggleItemPurchased(item.id)
                        }
                    )
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
}

