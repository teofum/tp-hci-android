package com.example.tphci.ui.products

import androidx.activity.ComponentActivity
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
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tphci.R
import com.example.tphci.data.model.Category
import com.example.tphci.data.model.ShoppingList
import com.example.tphci.ui.home.rememberWindowInfo
import com.example.tphci.ui.products.components.ManageCategoryBox
import com.example.tphci.ui.shopping_list.components.AddItemBox


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

    val windowInfo = rememberWindowInfo()
    val maxWidth = windowInfo.maxWidth


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
                Text("+ " + stringResource(R.string.add_category))
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
                stringResource(R.string.categories),
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
                                        category.name ?: stringResource(R.string.no_name),
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Text(
                                        stringResource(R.string.products_count, 0),
                                        style = MaterialTheme.typography.bodyMedium
                                    ) // TODO cantidad de productos por categ, tal vez está la función ya
                                }
                            }
                        }

                        Box {
                            IconButton(onClick = { expanded = true }) {
                                Icon(Icons.Default.MoreVert, contentDescription = stringResource(R.string.options))
                            }

                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text(stringResource(R.string.edit)) },
                                    leadingIcon = { Icon(Icons.Default.Edit, null) },
                                    onClick = {
                                        expanded = false
                                        showEditCategoryBox = true
                                        editingCategory = category
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text(stringResource(R.string.delete)) },
                                    leadingIcon = { Icon(Icons.Default.Delete, null) },
                                    onClick = { expanded = false } // TODO API
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
                title = stringResource(R.string.add_category),
                confirmButtonText = stringResource(R.string.add),
                onClose = { showAddCategoryBox = false },
                onConfirm = { name ->
                    onAddCategory(name) // TODO API
                    showAddCategoryBox = false
                }
            )
        }

        if (showEditCategoryBox && editingCategory != null) {
            ManageCategoryBox(
                title = stringResource(R.string.edit_category),
                initialName = editingCategory!!.name ?: "",
                confirmButtonText = stringResource(R.string.save),
                onClose = { showEditCategoryBox = false },
                onConfirm = { newName ->
                    // updateCategory(editingCategory!!.id, newName) // TODO API (están las variables "useState" ya creadas al principio)
                    showEditCategoryBox = false
                }
            )
        }

    }
    }