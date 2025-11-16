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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tphci.data.model.Category
import com.example.tphci.data.model.ShoppingList
import com.example.tphci.ui.products.components.ManageCategoryBox
import com.example.tphci.ui.shopping_list.components.AddItemBox

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    categories: List<Category>,
    onClose: () -> Unit,
    onAddCategory: (String) -> Unit
) {
    var categoryName by remember { mutableStateOf("") }

    var showAddItemScreen by remember { mutableStateOf(false) }

    var showAddCategoryBox by remember { mutableStateOf(false) }
    var showEditCategoryBox by remember { mutableStateOf(false) }
    var editingCategory by remember { mutableStateOf<Category?>(null) }


    // TODO api
//    LaunchedEffect(Unit) {
//        viewModel.getCategories()
//    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddCategoryBox = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.width(150.dp)
            ) {
                Text("+ Agregar categoría")
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                "Categorías",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))


            categories.forEach { category ->

                var expanded by remember { mutableStateOf(false) }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween

                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(Color(0xFFF1F1F1), RoundedCornerShape(12.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "📦", // TODO API
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Normal
                                )
                            }

                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.background
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 10.dp)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        category.name ?: "Sin nombre",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Text(
                                        "${0} productos",
                                        style = MaterialTheme.typography.bodyMedium
                                    ) // TODO cantidad de productos por categ, tal vez está la función ya
                                }
                            }
                        }

                        Box {
                            IconButton(onClick = { expanded = true }) {
                                Icon(Icons.Default.MoreVert, contentDescription = "Opciones")
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
                                        showEditCategoryBox = true
                                        editingCategory = category
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Eliminar") },
                                    leadingIcon = { Icon(Icons.Default.Delete, null) },
                                    onClick = { expanded = false } // TODO API
                                )
                            }
                        }
                    }
                }
            }

        }

        if (showAddCategoryBox) {
            ManageCategoryBox(
                title = "Agregar categoría",
                confirmButtonText = "Agregar",
                onClose = { showAddCategoryBox = false },
                onConfirm = { name ->
                    onAddCategory(name) // TODO API
                    showAddCategoryBox = false
                }
            )
        }

        if (showEditCategoryBox && editingCategory != null) {
            ManageCategoryBox(
                title = "Editar categoría",
                initialName = editingCategory!!.name ?: "",
                confirmButtonText = "Guardar",
                onClose = { showEditCategoryBox = false },
                onConfirm = { newName ->
                    // updateCategory(editingCategory!!.id, newName) // TODO API (están las variables "useState" ya creadas al principio)
                    showEditCategoryBox = false
                }
            )
        }

    }
    }