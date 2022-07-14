package pl.dszerszen.bestbefore.ui.navigation

sealed class NavScreen(val route: String) {
    object Main : NavScreen("main_screen")
    object Settings : NavScreen("settings_screen")
}