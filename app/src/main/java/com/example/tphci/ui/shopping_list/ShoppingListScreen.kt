package com.example.tphci.ui.shopping_list


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ShoppingListScreen(
    onOpenShareScreen: () -> Unit,
//    viewModel: HomeViewModel = viewModel(
//        factory = HomeViewModel.provideFactory(
//            (LocalContext.current.applicationContext as MyApplication).sessionManager,
//            (LocalContext.current.applicationContext as MyApplication).userRepository,
//            (LocalContext.current.applicationContext as MyApplication).shoppingRepository
//        )
//    )
) {
//    val uiState = viewModel.uiState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Mis Listas", style = MaterialTheme.typography.headlineMedium)


        // TODO share list
        Button(
            onClick = { onOpenShareScreen() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Compartir lista")
        }


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

//        Button(onClick = {
//            viewModel.addShoppingList(
//                name = listName.value,
//                description = listDescription.value,
//                recurring = recurring.value
//            )
//        }) {
//            Text("Agregar lista")
//        }
//
//
//        // listas
//        Button(onClick = { viewModel.getShoppingLists() }) {
//            Text("Obtener listas")
//        }
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        Text("Listas empty: ${uiState.shoppingLists.isEmpty()}")
//
//        Text("Listas:")
//
//        uiState.shoppingLists.forEach { list ->
//            Text("- ${list.name}")
//        }
    }
}