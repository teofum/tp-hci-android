package com.example.tphci.ui.products.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.tphci.R
import com.example.tphci.data.model.Category
import com.example.tphci.data.model.Product
import com.example.tphci.ui.EmojiPicker
import com.example.tphci.ui.home.rememberWindowInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductBox(
    categories: List<Category>,
    initial: Product? = null,
    onClose: () -> Unit,
    onConfirm: (product: Product) -> Unit
) {
    val windowInfo = rememberWindowInfo()
    val isTablet = windowInfo.maxWidth > 600.dp

    var productName by remember { mutableStateOf(initial?.name ?: "") }
    var categoryExpanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf(initial?.category) }
    var selectedEmoji by remember { mutableStateOf(initial?.emoji ?: "📦") }
    var showEmojiPicker by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onClose,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Column(
            modifier = if (isTablet) {
                Modifier
                    .widthIn(max = 600.dp)
                    .heightIn(max = 500.dp)
                    .background(MaterialTheme.colorScheme.background, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            } else {
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(16.dp)
            },
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.add_product),
                    style = MaterialTheme.typography.titleLarge
                )
                IconButton(onClick = onClose) {
                    Icon(Icons.Default.Close, contentDescription = stringResource(R.string.close))
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .background(Color.LightGray, RoundedCornerShape(20.dp))
                        .clickable { showEmojiPicker = true },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = selectedEmoji,
                        style = MaterialTheme.typography.headlineLarge
                    )
                }
            }

            if (showEmojiPicker) {
                androidx.compose.ui.window.Dialog(
                    onDismissRequest = { showEmojiPicker = false }
                ) {
                    EmojiPicker(
                        onSelect = {
                            selectedEmoji = it
                            showEmojiPicker = false
                        },
                        onDismiss = { showEmojiPicker = false }
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = productName,
                    onValueChange = { productName = it },
                    label = { Text(stringResource(R.string.product)) },
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ExposedDropdownMenuBox(
                    expanded = categoryExpanded,
                    onExpandedChange = { categoryExpanded = !categoryExpanded },
                    modifier = Modifier.weight(0.8f),
                ) {
                    OutlinedTextField(
                        value = selectedCategory?.name ?: stringResource(R.string.no_category),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(stringResource(R.string.category)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = categoryExpanded,
                        onDismissRequest = { categoryExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.no_category)) },
                            onClick = {
                                selectedCategory = null
                                categoryExpanded = false
                            }
                        )
                        categories.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat.name!!) },
                                onClick = {
                                    selectedCategory = cat
                                    categoryExpanded = false
                                }
                            )
                        }
                    }
                }

            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(onClick = onClose) { Text(stringResource(R.string.cancel)) }

                Button(onClick = {
                    if (productName.isNotBlank()) {
                        onConfirm(
                            Product(
                                initial?.id,
                                productName,
                                selectedEmoji,
                                null,
                                null,
                                selectedCategory
                            )
                        )
                    }
                }) {
                    Text(stringResource(R.string.add))
                }
            }
        }
    }
}



