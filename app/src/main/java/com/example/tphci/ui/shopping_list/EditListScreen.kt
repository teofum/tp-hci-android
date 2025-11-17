package com.example.tphci.ui.shopping_list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import com.example.tphci.R
import com.example.tphci.MyApplication
import com.example.tphci.ui.shopping_list.components.ManageListBox

@Composable
fun EditListScreen(
    listId: Int,
    onClose: () -> Unit,
    parentEntry: NavBackStackEntry,
    viewModel: ShoppingListViewModel = viewModel(
        viewModelStoreOwner = parentEntry,
        factory = ShoppingListViewModel.provideFactory(
            LocalContext.current.applicationContext as MyApplication,
        )
    )
) {
    val uiState = viewModel.uiState.collectAsState().value
    val list = uiState.shoppingLists.find { it.id == listId }

    if (list != null) {
        ManageListBox(
            title = stringResource(R.string.edit_lists),
            initialName = list.name,
            initialDescription = list.description,
            initialRecurring = list.recurring,
            confirmButtonText = stringResource(R.string.save_changes),
            onClose = onClose,
            onConfirm = { name, description, recurring ->
                // TODO: Update shopping list API call
                onClose()
            }
        )
    }
}
