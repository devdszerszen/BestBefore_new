package pl.dszerszen.bestbefore.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavBackStackEntry
import pl.dszerszen.bestbefore.R
import pl.dszerszen.bestbefore.util.ResString
import pl.dszerszen.bestbefore.util.StringValue

enum class NavScreen(val route: String, val bottomNavConfig: BottomNavConfig? = null) {
    Main("main_screen", BottomNavConfig(Icons.Default.Star, ResString(R.string.my_products), 1)),
    Settings("settings_screen", BottomNavConfig(Icons.Default.Settings, ResString(R.string.settings), 3)),
    AddProduct("add_product_screen"),
    Categories("categories_screen"),
//    ShoppingList(
//        "shopping_list_screen",
//        BottomNavConfig(Icons.Default.ShoppingCart, ResString(R.string.shopping_list), 2)
//    ),
}

class BottomNavConfig(val icon: ImageVector, val title: StringValue, val order: Int)

fun NavBackStackEntry.toNavScreen(): NavScreen? {
    return NavScreen.values().firstOrNull {
        this.destination.route == it.route
    }
}