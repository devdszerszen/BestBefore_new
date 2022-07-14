package pl.dszerszen.bestbefore.ui.navigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import pl.dszerszen.bestbefore.ui.add.AddProductScreen
import pl.dszerszen.bestbefore.ui.add.AddProductViewModel
import pl.dszerszen.bestbefore.ui.inapp.InAppEvent
import pl.dszerszen.bestbefore.ui.inapp.InAppEventHandler
import pl.dszerszen.bestbefore.ui.main.MainScreen
import pl.dszerszen.bestbefore.ui.main.MainViewModel
import pl.dszerszen.bestbefore.ui.settings.SettingsScreen
import pl.dszerszen.bestbefore.ui.settings.SettingsViewModel
import pl.dszerszen.bestbefore.ui.theme.BestBeforeTheme
import pl.dszerszen.bestbefore.util.Logger
import javax.inject.Inject

@AndroidEntryPoint
class NavActivity : ComponentActivity() {

    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private val permissionResults = Channel<Boolean>(Channel.CONFLATED)

    @Inject
    lateinit var logger: Logger

    @Inject
    lateinit var inAppEventHandler: InAppEventHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerPermissionResult { permissionResults.trySend(it) }
        setContent {
            val navController = rememberNavController()
            BestBeforeTheme {
                LaunchedEffect(Unit) {
                    inAppEventHandler.handleEvent {
                        logger.log("ACTIVITY: received event: $it")
                        when (val event = it) {
                            is InAppEvent.RequestPermission -> requestPermission(event)
                            is InAppEvent.ShowToast -> logger.log("Show toast")
                            is InAppEvent.Navigate -> navController.navigate(event.target.route)
                        }
                    }
                }
                NavHost(navController = navController, startDestination = NavScreen.Main.route) {
                    composable(route = NavScreen.Main.route) {
                        MainScreen(viewModels<MainViewModel>().value)
                    }
                    composable(route = NavScreen.Settings.route) {
                        SettingsScreen(viewModels<SettingsViewModel>().value)
                    }
                    composable(route = NavScreen.AddProduct.route) {
                        AddProductScreen(viewModels<AddProductViewModel>().value)
                    }
                }
            }
        }
    }

    private fun requestPermission(event: InAppEvent.RequestPermission) {
        lifecycleScope.launch {
            permissionLauncher.launch(event.permissionName)
            val isGranted = permissionResults.receive()
            event.isGrantedCallback(isGranted)

        }
    }

    private fun registerPermissionResult(onPermissionResult: (Boolean) -> Unit) {
        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {
            onPermissionResult(it)
        }
    }
}