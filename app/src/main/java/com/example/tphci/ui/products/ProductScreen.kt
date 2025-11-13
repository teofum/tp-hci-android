package com.example.tphci.ui.products

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ProductScreen() {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Productos", style = MaterialTheme.typography.headlineMedium)
        // Mostrar productos
    }
}