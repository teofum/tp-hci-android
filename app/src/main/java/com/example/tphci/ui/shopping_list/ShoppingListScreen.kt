package com.example.tphci.ui.shopping_list


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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tphci.MyApplication
import com.example.tphci.data.model.ShoppingList
import com.example.tphci.ui.home.rememberWindowInfo
import com.example.tphci.ui.shopping_list.components.ManageListBox

@Composable
fun ShoppingListScreen(
    onOpenShareScreen: () -> Unit,
    onOpenListDetails: (Int) -> Unit,
    viewModel: ShoppingListViewModel = viewModel(
        factory = ShoppingListViewModel.provideFactory(
            LocalContext.current.applicationContext as MyApplication,
        )
    )
) {
    val uiState = viewModel.uiState.collectAsState().value

    var showAddListBox by remember { mutableStateOf(false) }
    var showEditListBox by remember { mutableStateOf(false) }

    var editingList by remember { mutableStateOf<ShoppingList?>(null) }

    val windowInfo = rememberWindowInfo()
    val maxWidth = windowInfo.maxWidth
    val isTablet = maxWidth > 600.dp

    LaunchedEffect(Unit) {
        viewModel.startPolling()
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddListBox = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.width(150.dp)
            ) {
                Text("+ Agregar Lista")
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
                    "Listas",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                uiState.shoppingLists.forEach { list ->
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
                                        .background(
                                            Color(0xFFF1F1F1),
                                            RoundedCornerShape(12.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "📦", // TODO api
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
                                        .clickable { onOpenListDetails(list.id!!) }
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text(
                                            list.name,
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                        Text(
                                            list.description,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }
                            }

                            Box {
                                IconButton(onClick = { expanded = true }) {
                                    Icon(
                                        Icons.Default.MoreVert,
                                        contentDescription = "Opciones"
                                    )
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
                                            showEditListBox = true
                                            editingList = list
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Compartir") },
                                        leadingIcon = { Icon(Icons.Default.Share, null) },
                                        onClick = {
                                            expanded = false
                                            onOpenShareScreen()
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Eliminar") },
                                        leadingIcon = { Icon(Icons.Default.Delete, null) },
                                        onClick = { expanded = false } // TODO api
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        if (showAddListBox) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = if (isTablet) {
                        Modifier
                            .widthIn(max = 600.dp)
                            .heightIn(max = 400.dp)
                            .background(
                                MaterialTheme.colorScheme.background,
                                RoundedCornerShape(16.dp)
                            )
                            .padding(16.dp)
                    } else {
                        Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                            .padding(16.dp)
                    }
                ) {
                    ManageListBox(
                        title = "Agregar lista",
                        confirmButtonText = "Agregar",
                        onClose = { showAddListBox = false },
                        onConfirm = { name, description, recurring ->
                            viewModel.createShoppingList(
                                ShoppingList(
                                    name,
                                    description,
                                    recurring,
                                    "\uD83D\uDED2"
                                )
                            )
                            showAddListBox = false
                        }
                    )
                }
            }
        }

        if (showEditListBox && editingList != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = if (isTablet) {
                        Modifier
                            .widthIn(max = 600.dp)
                            .heightIn(max = 400.dp)
                            .background(
                                MaterialTheme.colorScheme.background,
                                RoundedCornerShape(16.dp)
                            )
                            .padding(16.dp)
                    } else {
                        Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                            .padding(16.dp)
                    }
                ) {
                    ManageListBox(
                        title = "Editar lista",
                        initialName = editingList!!.name,
                        initialDescription = editingList!!.description,
                        initialRecurring = editingList!!.recurring,
                        confirmButtonText = "Guardar",
                        onClose = {
                            showEditListBox = false
                            editingList = null
                        },
                        onConfirm = { name, description, recurring ->
//                            viewModel.(name, description, recurring) // TODO API
                            showEditListBox = false
                            editingList = null
                        }
                    )
                }
            }
        }
    }
}