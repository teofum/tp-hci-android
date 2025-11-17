package com.example.tphci.ui.products

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.tphci.data.model.Product
import com.example.tphci.ui.home.rememberWindowInfo
import com.example.tphci.ui.products.components.ManageCategoryBox


@Composable
fun CategoryScreen(
    products: List<Product>,
    categories: List<Category>,
    onClose: () -> Unit,
    onAdd: (Category) -> Unit,
    onUpdate: (Category) -> Unit,
    onDelete: (Category) -> Unit,
) {
    var showAddCategoryBox by remember { mutableStateOf(false) }
    var showEditCategoryBox by remember { mutableStateOf(false) }
    var editingCategory by remember { mutableStateOf<Category?>(null) }

    val windowInfo = rememberWindowInfo()
    val maxWidth = windowInfo.maxWidth

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
                                        text = category.emoji ?: "\uD83D\uDCE6",
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
                                            "${products.count { it.category?.id == category.id }} productos",
                                            style = MaterialTheme.typography.bodyMedium
                                        )
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
                                        text = {
                                            Text(
                                                "Eliminar",
                                                color = MaterialTheme.colorScheme.error
                                            )
                                        },
                                        leadingIcon = {
                                            Icon(
                                                Icons.Default.Delete,
                                                null,
                                                tint = MaterialTheme.colorScheme.error
                                            )
                                        },
                                        onClick = {
                                            expanded = false
                                            onDelete(category)
                                        }
                                    )
                                }
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
                onConfirm = { category ->
                    onAdd(category)
                    showAddCategoryBox = false
                }
            )
        }

        if (showEditCategoryBox && editingCategory != null) {
            ManageCategoryBox(
                title = "Editar categoría",
                initial = editingCategory,
                confirmButtonText = "Guardar",
                onClose = { showEditCategoryBox = false },
                onConfirm = { category ->
                    onUpdate(category)
                    showEditCategoryBox = false
                }
            )
        }
    }
}