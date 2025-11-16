package com.example.tphci.ui

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.window.core.layout.WindowSizeClass
import com.example.tphci.R
import com.example.tphci.ui.products.ProductScreen
import com.example.tphci.ui.profile.ProfileScreen
import com.example.tphci.ui.shareList.ShareListScreen
import com.example.tphci.ui.shopping_list.ShoppingListItemScreen
import com.example.tphci.ui.shopping_list.ShoppingListScreen
import com.example.tphci.ui.theme.TPHCITheme
import kotlinx.serialization.Serializable

@Serializable
object ShoppingLists

@Serializable
object Products

@Serializable
object Profile

@Serializable
object Share

@Serializable
data class ShoppingListItem(val listId: Long)


@Composable
fun AdaptiveApp() {
    TPHCITheme {

        val SHOPPING_LIST_ITEM = "shopping_list_item/{listId}"
        fun shoppingListItem(listId: Long) = "shopping_list_item/$listId"

        val adaptiveInfo = currentWindowAdaptiveInfo()
        val customNavSuiteType = with(adaptiveInfo) {
            if (windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)) {
                NavigationSuiteType.NavigationRail
            } else {
                NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(adaptiveInfo)
            }
        }

        val myNavigationSuiteItemColors = NavigationSuiteDefaults.itemColors(
            navigationBarItemColors = NavigationBarItemDefaults.colors(
                indicatorColor = MaterialTheme.colorScheme.primary,
                selectedIconColor = MaterialTheme.colorScheme.onPrimary
            ),
            navigationRailItemColors = NavigationRailItemDefaults.colors(
                indicatorColor = MaterialTheme.colorScheme.onPrimary,
                unselectedIconColor = MaterialTheme.colorScheme.onPrimary,
                unselectedTextColor = MaterialTheme.colorScheme.onPrimary
            )
        )

        val navController = rememberNavController()

        val entry by navController.currentBackStackEntryAsState()
        val currentDestination = entry?.destination

        NavigationSuiteScaffold(
            navigationSuiteColors = NavigationSuiteDefaults.colors(
                navigationBarContainerColor = MaterialTheme.colorScheme.surface,
                navigationRailContainerColor = MaterialTheme.colorScheme.primary,
            ),
            navigationSuiteItems = {
                item(
                    icon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.shopping_cart_24px),
                            contentDescription = stringResource(R.string.shopping_lists)
                        )
                    },
                    label = { Text(stringResource(R.string.shopping_lists)) },
                    selected = currentDestination?.hasRoute(
                        ShoppingLists::class
                    ) ?: false,
                    colors = myNavigationSuiteItemColors,
                    onClick = { navController.navigate(ShoppingLists) }
                )
                item(
                    icon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.package_2_24px),
                            contentDescription = stringResource(R.string.products)
                        )
                    },
                    label = { Text(stringResource(R.string.products)) },
                    selected = currentDestination?.hasRoute(
                        Products::class
                    ) ?: false,
                    colors = myNavigationSuiteItemColors,
                    onClick = { navController.navigate(Products) }
                )
                item(
                    icon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.person_24px),
                            contentDescription = stringResource(R.string.profile)
                        )
                    },
                    label = { Text(stringResource(R.string.profile)) },
                    selected = currentDestination?.hasRoute(
                        Profile::class
                    ) ?: false,
                    colors = myNavigationSuiteItemColors,
                    onClick = { navController.navigate(Profile) }
                )
            },
            layoutType = customNavSuiteType
        ) {
            NavHost(navController = navController, startDestination = ShoppingLists) {
                composable<ShoppingLists> {
                    ShoppingListScreen(
                        onOpenListDetails = { listId ->
                            navController.navigate(ShoppingListItem(listId))
                        }
                    )
                }
                composable<Products> { ProductScreen() }
                composable<Profile> { ProfileScreen() }
                composable<ShoppingListItem> { entry ->
                    val args = entry.arguments!!
                    val listId = args.getLong("listId")
                    ShoppingListItemScreen(
                        onOpenShareScreen = { navController.navigate(Share) }
                    )
                }
                composable<Share> { // TODO make the dialog fullscreen (TODO connect to /shareList)
                    ShareListScreen(
                        selectedUsers = emptyList(),
                        suggestedUsers = emptyList(),
                        searchQuery = "",
                        onSearchQueryChange = {},
                        onUserToggle = {},
                        onRemoveSelectedUser = {},
                        onBackClick = { navController.popBackStack() },
                        onDoneClick = { navController.popBackStack() },
                    )
                }
            }
        }
    }
}