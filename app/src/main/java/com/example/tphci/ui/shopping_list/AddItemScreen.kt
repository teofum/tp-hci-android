package com.example.tphci.ui.shopping_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tphci.MyApplication
import com.example.tphci.ui.home.rememberWindowInfo
import com.example.tphci.ui.shopping_list.components.AddItemBox

@Composable
fun AddItemScreen(
    listId: Int,
    onClose: () -> Unit,
    parentEntry: androidx.navigation.NavBackStackEntry,
    viewModel: ListItemsViewModel = viewModel(
        viewModelStoreOwner = parentEntry,
        factory = ListItemsViewModel.provideFactory(
            LocalContext.current.applicationContext as MyApplication,
            listId
        )
    )
) {
    val uiState = viewModel.uiState.collectAsState().value
    val windowInfo = rememberWindowInfo()
    val isTablet = windowInfo.maxWidth > 600.dp

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
                    .background(MaterialTheme.colorScheme.background, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            } else {
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(16.dp)
            }
        ) {
            AddItemBox(
                onClose = onClose,
                onAdd = { item ->
                    viewModel.addListItem(item)
                    onClose()
                },
                products = uiState.products
            )
        }
    }
}
