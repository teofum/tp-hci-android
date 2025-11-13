package com.example.tphci.ui.home

import BottomBar
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.tphci.ui.products.ProductScreen
import com.example.tphci.ui.profile.ProfileScreen
import com.example.tphci.ui.shareList.ShareListScreen
import com.example.tphci.ui.shopping_list.ShoppingListScreen

@Composable
fun HomeScreen(
    onLogout: () -> Unit
) {
    var currentRoute by remember { mutableStateOf("shopping_list") }
    var showShareScreen by remember { mutableStateOf(false) }

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
                    onOpenShareScreen = { showShareScreen = true }
                )
                "products" -> ProductScreen()
                "profile" -> ProfileScreen(
                    onLogout = onLogout
                )
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
        }
    }
}
