package com.example.tphci.ui.shopping_list


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.ui.unit.dp

@Composable
fun ShoppingListScreen(
    onOpenListDetails: (Long) -> Unit,
//    viewModel: HomeViewModel = viewModel(
//        factory = HomeViewModel.provideFactory(
//            (LocalContext.current.applicationContext as MyApplication).sessionManager,
//            (LocalContext.current.applicationContext as MyApplication).userRepository,
//            (LocalContext.current.applicationContext as MyApplication).shoppingRepository
//        )
//    )
) {
//    val uiState = viewModel.uiState

    var showAddListBox by remember { mutableStateOf(false) }

//    LaunchedEffect(Unit) {
//        viewModel.getShoppingLists()
//    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddListBox = true }
            ) {
                Text("Agregar Lista")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(1f))

                Text(
                    "Listas",
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


//            uiState.shoppingLists.forEach { list ->
//                Card(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(vertical = 4.dp)
//                        .clickable { onOpenListDetails(list.id.toLong()) }
//                ) {
//                    Column(modifier = Modifier.padding(12.dp)) {
//                        Text(list.name, style = MaterialTheme.typography.titleMedium)
//                        Text(list.description, style = MaterialTheme.typography.bodyMedium)
//                    }
//                }
//            }
        }

//        if (showAddListBox) {
//            AddListBox(
//                onClose = { showAddListBox = false },
//                onAdd = { name, description, recurring ->
//                    viewModel.addShoppingList(name, description, recurring)
//                    showAddListBox = false
//                }
//            )
//        }
    }
}