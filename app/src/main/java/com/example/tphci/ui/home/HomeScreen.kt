package com.example.tphci.ui.home

import BottomBar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tphci.MyApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonObject
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.tphci.ui.products.ProductScreen
import com.example.tphci.ui.profile.ProfileScreen
import com.example.tphci.ui.shareList.ShareListScreen
import com.example.tphci.ui.shopping_list.ShoppingListItemScreen
import com.example.tphci.ui.shopping_list.ShoppingListScreen

@Composable
fun HomeScreen() {
    var currentRoute by remember { mutableStateOf("shopping_list") }
    var showShareScreen by remember { mutableStateOf(false) }
    var selectedListId by remember { mutableStateOf<Long?>(null) }
    var showListDetails by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            BottomBar(
                currentRoute = currentRoute,
                onTabSelected = { route -> currentRoute = route }
            )
        }
    ) { innerPadding ->
        Box(Modifier.padding(innerPadding)) {
            when (currentRoute) {
                "shopping_list" -> ShoppingListScreen(
                    onOpenListDetails = { id ->
                        selectedListId = id
                        showListDetails = true
                    }
                )
                "products" -> ProductScreen()
                "profile" -> ProfileScreen()
            }

            if (showShareScreen) {
                ShareListScreen(
                    selectedUsers = emptyList(),
                    suggestedUsers = emptyList(),
                    searchQuery = "",
                    onSearchQueryChange = {},
                    onUserToggle = {},
                    onRemoveSelectedUser = {},
                    onBackClick = { showShareScreen = false },
                    onDoneClick = { showShareScreen = false },
                )
            }

            if (showListDetails && selectedListId != null) {
                ShoppingListItemScreen(
                    listId = selectedListId!!,
                    onClose = { showListDetails = false },
                    onOpenShareScreen = { showShareScreen = true },
                )
            }
        }
    }
}



//@Composable
//fun HomeScreen(
//    viewModel: HomeViewModel = viewModel(
//        factory = HomeViewModel.provideFactory(
//            (LocalContext.current.applicationContext as MyApplication).sessionManager,
//            (LocalContext.current.applicationContext as MyApplication).userRepository,
//            (LocalContext.current.applicationContext as MyApplication).shoppingRepository
//        )
//    )
//) {
//    val uiState = viewModel.uiState
//
//
//
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // TODO
////            Button(onClick = { viewModel.getCategories() }) {
////                Text("Obtener categorías")
////            }
//
//            Spacer(modifier = Modifier.height(8.dp))
//            Text("Categorías:")
//
//            uiState.categories.forEach { category ->
//                Text("- ${category.name}")
//            }
//        }
//    }
//
//    LaunchedEffect(uiState.isAuthenticated) {
//        if (uiState.isAuthenticated) {
//            viewModel.getProducts()
//            viewModel.getShoppingLists()
////             viewModel.getCategories() // TODO
//        }
//    }
//}
