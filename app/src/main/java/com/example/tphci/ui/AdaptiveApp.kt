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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.window.core.layout.WindowSizeClass
import com.example.tphci.navigation.AppDestinations
import com.example.tphci.ui.products.ProductScreen
import com.example.tphci.ui.profile.ProfileScreen
import com.example.tphci.ui.shopping_list.ShoppingListScreen
import com.example.tphci.ui.theme.TPHCITheme
import kotlinx.serialization.Serializable

@Serializable
object Home

@Serializable
object ShoppingLists

@Serializable
object Products

@Composable
fun AdaptiveApp() {
    TPHCITheme {
        val adaptiveInfo = currentWindowAdaptiveInfo()
        val customNavSuiteType = with(adaptiveInfo) {
            if (windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)) {
                NavigationSuiteType.NavigationRail
            } else {
                NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(adaptiveInfo)
            }
        }

        var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.SHOPPING_LISTS) }

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
        NavigationSuiteScaffold(
            navigationSuiteColors = NavigationSuiteDefaults.colors(
                navigationRailContainerColor = MaterialTheme.colorScheme.primary
            ),
            navigationSuiteItems = {
                AppDestinations.entries.forEach {
                    item(
                        icon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(it.icon),
                                contentDescription = stringResource(it.contentDescription)
                            )
                        },
                        label = { Text(stringResource(it.label)) },
                        selected = it == currentDestination,
                        colors = myNavigationSuiteItemColors,
                        onClick = { currentDestination = it }
                    )
                }
            },
            layoutType = customNavSuiteType
        ) {
            when (currentDestination) {
                AppDestinations.SHOPPING_LISTS -> ShoppingListScreen(onOpenShareScreen = {})
                AppDestinations.PRODUCTS -> ProductScreen()
                AppDestinations.PROFILE -> ProfileScreen()
            }
        }
    }
}