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
import androidx.compose.runtime.remember
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
import com.example.tphci.ui.products.AddProductScreen
import com.example.tphci.ui.products.CategoriesScreen
import com.example.tphci.ui.products.EditProductScreen
import com.example.tphci.ui.products.ProductScreen
import com.example.tphci.ui.profile.ProfileScreen
import com.example.tphci.ui.shareList.ShareListScreen
import com.example.tphci.ui.shopping_list.AddItemScreen
import com.example.tphci.ui.shopping_list.AddListScreen
import com.example.tphci.ui.shopping_list.EditListScreen
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
data class Item(val listId: Int)

@Serializable
object AddProduct

@Serializable
data class EditProduct(val productId: Int)

@Serializable
object Categories

@Serializable
object AddList

@Serializable
data class EditList(val listId: Int)

@Serializable
data class AddItem(val listId: Int)

@Serializable
object Settings


@Composable
fun AdaptiveApp() {
    TPHCITheme {
        fun item(listId: Int) = "item/$listId"

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
                        onOpenShareScreen = { navController.navigate(Share) },
                        onOpenListDetails = { listId -> navController.navigate(Item(listId)) },
                        onNavigateToAddList = { navController.navigate(AddList) },
                        onNavigateToEditList = { listId -> navController.navigate(EditList(listId)) },
                        onNavigateToSettings = { navController.navigate(Settings) }
                    )
                }
                composable<Products> { 
                    ProductScreen(
                        onNavigateToAddProduct = { navController.navigate(AddProduct) },
                        onNavigateToEditProduct = { productId -> navController.navigate(EditProduct(productId)) },
                        onNavigateToCategories = { navController.navigate(Categories) },
                        onNavigateToSettings = { navController.navigate(Settings) }
                    )
                }
                composable<Profile> { ProfileScreen() }
                composable<Item> { entry ->
                    val args = entry.arguments!!
                    val listId = args.getInt("listId")
                    ShoppingListItemScreen(
                        onOpenShareScreen = { navController.navigate(Share) },
                        listId = listId,
                        onClose = { navController.popBackStack() },
                        onNavigateToAddItem = { navController.navigate(AddItem(listId)) }
                    )
                }
                composable<Share> { // TODO make the dialog fullscreen (TODO connect to /shareList)
                    ShareListScreen(
                        selectedShareUsers = emptyList(),
                        suggestedShareUsers = emptyList(),
                        searchQuery = "",
                        onSearchQueryChange = {},
                        onShareUserToggle = {},
                        onRemoveSelectedShareUser = {},
                        onBackClick = { navController.popBackStack() },
                        onDoneClick = { navController.popBackStack() },
                    )
                }
                composable<AddProduct> { backStackEntry ->
                    val parentEntry = remember(backStackEntry) {
                        navController.getBackStackEntry<Products>()
                    }
                    AddProductScreen(
                        onClose = { navController.popBackStack() },
                        parentEntry = parentEntry
                    )
                }
                composable<EditProduct> { backStackEntry ->
                    val args = backStackEntry.arguments!!
                    val productId = args.getInt("productId")
                    val parentEntry = remember(backStackEntry) {
                        navController.getBackStackEntry<Products>()
                    }
                    EditProductScreen(
                        productId = productId,
                        onClose = { navController.popBackStack() },
                        parentEntry = parentEntry
                    )
                }
                composable<Categories> { backStackEntry ->
                    val parentEntry = remember(backStackEntry) {
                        navController.getBackStackEntry<Products>()
                    }
                    CategoriesScreen(
                        onClose = { navController.popBackStack() },
                        parentEntry = parentEntry
                    )
                }
                composable<AddList> { backStackEntry ->
                    val parentEntry = remember(backStackEntry) {
                        navController.getBackStackEntry<ShoppingLists>()
                    }
                    AddListScreen(
                        onClose = { navController.popBackStack() },
                        parentEntry = parentEntry
                    )
                }
                composable<EditList> { backStackEntry ->
                    val args = backStackEntry.arguments!!
                    val listId = args.getInt("listId")
                    val parentEntry = remember(backStackEntry) {
                        navController.getBackStackEntry<ShoppingLists>()
                    }
                    EditListScreen(
                        listId = listId,
                        onClose = { navController.popBackStack() },
                        parentEntry = parentEntry
                    )
                }
                composable<AddItem> { backStackEntry ->
                    val args = backStackEntry.arguments!!
                    val listId = args.getInt("listId")
                    val parentEntry = remember(backStackEntry) {
                        navController.getBackStackEntry<Item>(Item(listId))
                    }
                    AddItemScreen(
                        listId = listId,
                        onClose = { navController.popBackStack() },
                        parentEntry = parentEntry
                    )
                }
                composable<Settings> {
                    SettingsScreen(
                        onClose = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}