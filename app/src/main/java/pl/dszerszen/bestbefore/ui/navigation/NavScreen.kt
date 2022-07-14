package pl.dszerszen.bestbefore.ui.navigation

sealed class NavScreen(val route: String) {
    object Main : NavScreen("main_screen")
    object Settings : NavScreen("settings_screen")
    object AddProduct : NavScreen("add_product_screen")
}