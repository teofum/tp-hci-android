package com.example.tphci.ui.shopping_list


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tphci.MyApplication
import com.example.tphci.ui.home.HomeViewModel

@Composable
fun ShoppingListScreen(
    viewModel: HomeViewModel = viewModel(
        factory = HomeViewModel.provideFactory(
            (LocalContext.current.applicationContext as MyApplication).sessionManager,
            (LocalContext.current.applicationContext as MyApplication).userRepository,
            (LocalContext.current.applicationContext as MyApplication).shoppingRepository
        )
    )
) {

    val uiState = viewModel.uiState


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Mis Listas", style = MaterialTheme.typography.headlineMedium)



        // agregar lista
        val listName = remember { mutableStateOf("") }
        val listDescription = remember { mutableStateOf("") }
        val recurring = remember { mutableStateOf(false) }


        OutlinedTextField(
            value = listName.value,
            onValueChange = { listName.value = it },
            label = { Text("Nombre de la lista") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = listDescription.value,
            onValueChange = { listDescription.value = it },
            label = { Text("Descriptión de la lista") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Recurrente")
            Switch(
                checked = recurring.value,
                onCheckedChange = { recurring.value = it },
                colors = SwitchDefaults.colors(checkedThumbColor = Color.Green)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            viewModel.addShoppingList(
                name = listName.value,
                description = listDescription.value,
                recurring = recurring.value
            )
        }) {
            Text("Agregar lista")
        }


        // listas
        Button(onClick = { viewModel.getShoppingLists() }) {
            Text("Obtener listas")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("Listas empty: ${uiState.shoppingLists.isEmpty()}")

        Text("Listas:")

        uiState.shoppingLists.forEach { list ->
            Text("- ${list.name}")
        }


    }
}