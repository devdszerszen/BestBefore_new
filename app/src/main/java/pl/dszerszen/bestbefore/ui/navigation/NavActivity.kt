package pl.dszerszen.bestbefore.ui.navigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import pl.dszerszen.bestbefore.ui.main.MainScreen
import pl.dszerszen.bestbefore.ui.main.MainViewModel
import pl.dszerszen.bestbefore.ui.settings.SettingsScreen
import pl.dszerszen.bestbefore.ui.settings.SettingsViewModel
import pl.dszerszen.bestbefore.ui.theme.BestBeforeTheme
import javax.inject.Inject

@AndroidEntryPoint
class NavActivity : ComponentActivity() {

    @Inject
    lateinit var navigationHandler: NavigationHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            BestBeforeTheme {
                LaunchedEffect(Unit) {
                    navigationHandler.handleEvent {
                        navController.navigate(it.route)
                    }
                }
                NavHost(navController = navController, startDestination = NavScreen.Main.route) {
                    composable(route = NavScreen.Main.route) {
                        MainScreen(viewModels<MainViewModel>().value)
                    }
                    composable(route = NavScreen.Settings.route) {
                        SettingsScreen(viewModels<SettingsViewModel>().value)
                    }
                }
            }
        }
    }
}