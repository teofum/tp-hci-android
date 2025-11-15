package com.example.tphci.ui.shopping_list


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tphci.R
import com.example.tphci.MyApplication
import com.example.tphci.ui.home.HomeViewModel
import com.example.tphci.ui.shopping_list.components.AddListBox

@Composable
fun ShoppingListScreen(
    onOpenListDetails: (Long) -> Unit,
    viewModel: HomeViewModel = viewModel(
        factory = HomeViewModel.provideFactory(
            (LocalContext.current.applicationContext as MyApplication).sessionManager,
            (LocalContext.current.applicationContext as MyApplication).userRepository,
            (LocalContext.current.applicationContext as MyApplication).shoppingRepository
        )
    )
) {

    val uiState = viewModel.uiState

    var showAddListBox by remember { mutableStateOf(false) }


    LaunchedEffect(Unit) {
        viewModel.getShoppingLists()
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddListBox = true }
            ) {
                Text(stringResource(R.string.add_list))
            }
        }
    ) { innerPadding ->



        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(1f))

                Text(
                    stringResource(R.string.shopping_lists),
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.weight(1f))

                IconButton(onClick = { /* TODO */ }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar listas"
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))


            uiState.shoppingLists.forEach { list ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { onOpenListDetails(list.id.toLong()) }
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(list.name, style = MaterialTheme.typography.titleMedium)
                        Text(list.description, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }

        if (showAddListBox) {
            AddListBox(
                onClose = { showAddListBox = false },
                onAdd = { name, description, recurring ->
                    viewModel.addShoppingList(name, description, recurring)
                    showAddListBox = false
                }
            )
        }

    }
    }