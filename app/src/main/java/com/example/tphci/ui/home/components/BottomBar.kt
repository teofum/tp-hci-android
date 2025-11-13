import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.vector.ImageVector


data class BottomNavItem(
    val route: String,
    val label: String,
    val iconRes: ImageVector
)

@Composable
fun BottomBar(
    currentRoute: String,
    onTabSelected: (String) -> Unit
) {
    val items = listOf(
        BottomNavItem("shopping_list", "Listas", Icons.Filled.List),
        BottomNavItem("products", "Productos", Icons.Filled.ShoppingCart),
        BottomNavItem("profile", "Perfil", Icons.Filled.Person)
    )

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = { onTabSelected(item.route) },
                label = { Text(item.label) },
                icon = { Icon(imageVector = item.iconRes, contentDescription = item.label) }
            )
        }
    }
}
