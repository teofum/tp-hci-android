package com.example.tphci.ui.shopping_list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.example.tphci.data.model.Item
import com.example.tphci.data.model.Product
import com.example.tphci.ui.EmojiPicker

@Composable
fun AddItemBox(
    onClose: () -> Unit,
    onAdd: (item: Item) -> Unit,
    products: List<Product>
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    MaterialTheme.colorScheme.background
                )
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Agregar item",
                    style = MaterialTheme.typography.titleLarge
                )
                IconButton(onClick = onClose) {
                    Icon(Icons.Default.Close, contentDescription = "Cerrar")
                }
            }

            var quantity by remember { mutableStateOf(1) }
            var unit by remember { mutableStateOf("") }
            var product by remember { mutableStateOf<Product?>(null) }

            var expanded by remember { mutableStateOf(false) }
            var textFieldSize by remember { mutableStateOf(androidx.compose.ui.geometry.Size.Zero) }
            val icon =
                if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = quantity.toString(),
                    onValueChange = {
                        quantity = it.filter { char -> char.isDigit() }.toIntOrNull() ?: 0
                    },
                    label = { Text("Cantidad") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = unit,
                    onValueChange = { unit = it },
                    label = { Text("Unidad") },
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    OutlinedTextField(
                        value = product?.name ?: "",
                        onValueChange = { },
                        readOnly = true,
                        label = { Text("Producto") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .onSizeChanged {
                                textFieldSize = it.toSize()
                            },
                        trailingIcon = {
                            IconButton(onClick = { expanded = !expanded }) {
                                Icon(icon, "contentDescription")
                            }
                        }
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.width(with(LocalDensity.current) { textFieldSize.width.toDp() })
                    ) {
                        products.forEach { p ->
                            DropdownMenuItem(
                                text = { Text(p.name ?: "") },
                                onClick = {
                                    product = p
                                    expanded = false
                                })
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(onClick = onClose) { Text("Cancelar") }

                Button(onClick = {
                    product?.let { onAdd(Item(quantity, unit, it)) }
                }) {
                    Text("Agregar")
                }
            }
        }
    }
}


