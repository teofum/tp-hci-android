package com.example.tphci.ui.shopping_list.components

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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
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
import com.example.tphci.data.model.ShoppingList
import com.example.tphci.ui.EmojiPicker
import com.example.tphci.ui.home.rememberWindowInfo

@Composable
fun ManageListBox(
    title: String,
    initial: ShoppingList? = null,
    confirmButtonText: String,
    onClose: () -> Unit,
    onConfirm: (ShoppingList) -> Unit
) {
    var name by remember { mutableStateOf(initial?.name ?: "") }
    var description by remember { mutableStateOf(initial?.description ?: "") }
    var recurring by remember { mutableStateOf(initial?.recurring ?: false) }
    var selectedEmoji by remember { mutableStateOf(initial?.emoji ?: "🛒") }
    var showEmojiPicker by remember { mutableStateOf(false) }

    val windowInfo = rememberWindowInfo()
    val isTablet = windowInfo.maxWidth > 600.dp

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
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(title, style = MaterialTheme.typography.titleLarge)
                IconButton(onClick = onClose) {
                    Icon(Icons.Default.Close, contentDescription = stringResource(R.string.cancel))
                }
            }

            if (showEmojiPicker) {
                Dialog(
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

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(stringResource(R.string.list_name)) },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text(stringResource(R.string.description)) },
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(stringResource(R.string.recurring) + " ")
                Switch(
                    checked = recurring,
                    onCheckedChange = { recurring = it }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(onClick = onClose) { Text(stringResource(R.string.cancel)) }

                Button(
                    onClick = {
                        if (name.isNotBlank()) {
                            val list = ShoppingList(
                                initial?.id,
                                name,
                                description,
                                recurring,
                                selectedEmoji,
                                null,
                                null,
                                null,
                                null,
                                null
                            )
                            onConfirm(list)
                        }
                    }
                ) {
                    Text(confirmButtonText)
                }
            }
        }
    }
}