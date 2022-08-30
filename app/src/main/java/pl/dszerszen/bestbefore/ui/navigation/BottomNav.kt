package pl.dszerszen.bestbefore.ui.navigation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import pl.dszerszen.bestbefore.ui.theme.BestBeforeTheme

@Composable
fun BottomBar(navController: NavController) {
    val currentEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentEntry?.destination?.route

    BottomAppBar {
        NavScreen.values()
            .apply { sortBy { it.bottomNavConfig?.order } }
            .forEach { navScreen ->
                NavIcon(navScreen, currentRoute) {
                    if (currentRoute == navScreen.route) {
                        return@NavIcon
                    }
                    navController.navigate(route = navScreen.route) {
                        currentRoute?.let {
                            popUpTo(it) {
                                inclusive = true
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
    }
}

@Composable
private fun RowScope.NavIcon(
    item: NavScreen,
    currentRoute: String?,
    onClick: () -> Unit,
) {
    if (item.bottomNavConfig != null) {
        BottomNavigationItem(
            modifier = Modifier.weight(1f),
            selected = currentRoute == item.route,
            icon = { Icon(imageVector = item.bottomNavConfig.icon, contentDescription = null) },
            label = { Text(item.bottomNavConfig.title.get()) },
            onClick = onClick
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Day")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Night")
@Composable
fun NavIconsPreview() {
    BestBeforeTheme(previewMode = true) {
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            NavScreen.values().filter { it.bottomNavConfig != null }.forEach {
                NavIcon(it, "") {}
            }
        }
    }
}