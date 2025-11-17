package com.example.tphci.ui.shopping_list

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import com.example.tphci.R
import com.example.tphci.MyApplication
import com.example.tphci.data.model.ShoppingList
import com.example.tphci.ui.shopping_list.components.ManageListBox

@Composable
fun AddListScreen(
    onClose: () -> Unit,
    parentEntry: NavBackStackEntry,
    viewModel: ShoppingListViewModel = viewModel(
        viewModelStoreOwner = parentEntry,
        factory = ShoppingListViewModel.provideFactory(
            LocalContext.current.applicationContext as MyApplication,
        )
    )
) {
    ManageListBox(
        title = stringResource(R.string.add_list),
        confirmButtonText = stringResource(R.string.add),
        onClose = onClose,
        onConfirm = { list ->
            viewModel.createShoppingList(list)
            onClose()
        }
    )
}
